package internal

import (
	"bufio"
	"bytes"
	"net/http"
	"strconv"

	"github.com/google/gopacket"
	"github.com/google/gopacket/layers"
)

type DecodeLayersVar struct {
	ethLayer layers.Ethernet
	ipLayer  layers.IPv4
	tcpLayer layers.TCP
	tlsLayer layers.TLS
	udpLayer layers.UDP
	dnsLayer layers.DNS
}

type Packet struct {
	SrcMac          string
	DstMac          string
	SrcIP           string
	DstIP           string
	SrcPort         int
	DstPort         int
	SYN             string
	ACK             string
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
			myPacket.SYN = strconv.FormatBool(d.tcpLayer.SYN)
			myPacket.ACK = strconv.FormatBool(d.tcpLayer.ACK)
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

		}
	}
	ch <- myPacket
}
