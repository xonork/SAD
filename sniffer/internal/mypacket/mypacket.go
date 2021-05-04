package mypacket

import (
	"bufio"
	"bytes"
	"net/http"
	"strconv"

	"github.com/google/gopacket"
	"github.com/google/gopacket/layers"
)

/*
	Defines the layers which
	are going to be decoded
*/
type DecodeLayersVar struct {
	ethLayer layers.Ethernet
	ipLayer  layers.IPv4
	tcpLayer layers.TCP
	tlsLayer layers.TLS
	udpLayer layers.UDP
	dnsLayer layers.DNS
}

/*
	Defines the struct Packet,
	which contains information
	about the analized packet
*/
type Packet struct {
	SrcMac          string
	DstMac          string
	SrcIP           string
	DstIP           string
	SrcPort         int
	DstPort         int
	SYN             bool
	ACK             bool
	HTTPReques      http.Request
	HighestProtocol string
	DNS             Dns
}

type Dns struct {
	Answers   []layers.DNSResourceRecord
	Questions []layers.DNSQuestion
	OpCode    string
}

func (d *DecodeLayersVar) FasterDecoder(packet gopacket.Packet, ch chan<- Packet) {

	parser := gopacket.NewDecodingLayerParser(
		layers.LayerTypeEthernet,
		&d.ethLayer,
		&d.ipLayer,
		&d.tcpLayer,
		&d.tlsLayer,
		&d.udpLayer,
		&d.dnsLayer,
	)
	foundLayerTypes := []gopacket.LayerType{}
	myPacket := Packet{}
	/*err :=*/ parser.DecodeLayers(packet.Data(), &foundLayerTypes)
	/*if err != nil {
		fmt.Println("Trouble decoding layers: ", err)
	}*/
	for _, layerType := range foundLayerTypes {

		switch layerType {
		case layers.LayerTypeEthernet:
			myPacket.SrcMac = d.ethLayer.SrcMAC.String()
			myPacket.DstMac = d.ethLayer.DstMAC.String()
			myPacket.HighestProtocol = "Ethernet"
		case layers.LayerTypeIPv4:
			myPacket.SrcIP = d.ipLayer.SrcIP.String()
			myPacket.DstIP = d.ipLayer.DstIP.String()
			myPacket.HighestProtocol = "IPv4"
		case layers.LayerTypeTCP:
			myPacket.SrcPort = int(d.tcpLayer.SrcPort)
			myPacket.DstPort = int(d.tcpLayer.DstPort)
			myPacket.SYN = d.tcpLayer.SYN
			myPacket.ACK = d.tcpLayer.ACK
			myPacket.HighestProtocol = "TCP"
			if payload := d.tcpLayer.Payload; len(payload) != 0 {
				reader := bufio.NewReader(bytes.NewReader(payload))
				httpReq, err := http.ReadRequest(reader)
				if err == nil {
					myPacket.HTTPReques = *httpReq
					myPacket.HighestProtocol = "HTTP"
				}
			}
		case layers.LayerTypeTLS:
			myPacket.HighestProtocol = "TLS"
		case layers.LayerTypeUDP:
			myPacket.SrcPort = int(d.udpLayer.SrcPort)
			myPacket.DstPort = int(d.udpLayer.DstPort)
			myPacket.HighestProtocol = "UDP"
		case layers.LayerTypeDNS:
			myPacket.DNS.Answers = d.dnsLayer.Answers
			myPacket.DNS.Questions = d.dnsLayer.Questions
			myPacket.DNS.OpCode = d.dnsLayer.OpCode.String()
			myPacket.HighestProtocol = "DNS"

		}
	}
	ch <- myPacket
}

func PacketToString(p Packet) string {
	var str string

	switch p.HighestProtocol {
	case "Ethernet":
		str = p.SrcMac + "\t" + p.DstMac + "\t" + p.HighestProtocol + "\t"
	case "IPv4":
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol
	case "TCP":
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol + "\t" +
			strconv.Itoa(p.SrcPort) + " -> " + strconv.Itoa(p.DstPort) + " "
		switch {
		case p.ACK && p.SYN:
			str = str + "[ SYN, ACK ]"
		case p.ACK:
			str = str + "[ ACK ]"
		case p.SYN:
			str = str + "[ SYN ]"
		}
	case "UDP":
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol + "\t" +
			strconv.Itoa(p.SrcPort) + " -> " + strconv.Itoa(p.DstPort)
	case "HTTP":
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol + "\t" +
			p.HTTPReques.Method
	case "DNS":
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol + "\t" +
			"OpCode: " + p.DNS.OpCode + " "
		for _, query := range p.DNS.Questions {
			str = str + string(query.Name) + " "
		}

	default:
		str = p.SrcIP + "\t" + p.DstIP + "\t" + p.HighestProtocol
	}

	return str
}
