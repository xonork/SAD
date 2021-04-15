package main

import (
	"bufio"
	"bytes"
	"flag"
	"fmt"
	"log"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/google/gopacket"
	"github.com/google/gopacket/layers"
	"github.com/google/gopacket/pcap"
)

var (
	device       string = "enp0s3"
	snapshot_len int32  = 1024
	promiscuous  bool   = false
	err          error
	timeout      time.Duration = 30 * time.Second
	handle       *pcap.Handle
	ethLayer     layers.Ethernet
	ipLayer      layers.IPv4
	tcpLayer     layers.TCP
	tlsLayer     layers.TLS
	udpLayer     layers.UDP
	dnsLayer     layers.DNS
)

func main() {
	filter := flag.String("filter", "", "BPF syntax")
	flag.Parse()
	handle, err = pcap.OpenLive(device, snapshot_len, promiscuous, timeout)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("hola")
	defer handle.Close()

	err = handle.SetBPFFilter(*filter)
	if err != nil {
		log.Fatal(err)
	}

	packetSource := gopacket.NewPacketSource(handle, handle.LinkType())
	for packet := range packetSource.Packets() {
		fasterDecoder(packet)
	}
}

func printPacketInfo(packet gopacket.Packet) {
	ethernetLayer := packet.Layer(layers.LayerTypeEthernet)
	if ethernetLayer != nil {
		fmt.Println("Ethernet layer detected")
		ethernetPacket, _ := ethernetLayer.(*layers.Ethernet)
		fmt.Println("Source Mac: ", ethernetPacket.SrcMAC)
		fmt.Println("Destination Mac: ", ethernetPacket.DstMAC)
		fmt.Println("Ethernet type: ", ethernetPacket.EthernetType)
		fmt.Println()
	}

	ipLayer := packet.Layer(layers.LayerTypeIPv4)
	if ipLayer != nil {
		fmt.Println("IPv4 layer detected.")
		ip, _ := ipLayer.(*layers.IPv4)

		fmt.Printf("From %s to %s\n", ip.SrcIP, ip.DstIP)
		fmt.Println("Protocol: ", ip.Protocol)
		fmt.Println()
	}

	tcpLayer := packet.Layer(layers.LayerTypeTCP)
	if tcpLayer != nil {
		fmt.Println(("TCP layer detected"))
		tcp, _ := tcpLayer.(*layers.TCP)

		fmt.Printf("From port %d to %d\n", tcp.SrcPort, tcp.DstPort)
		fmt.Println("Sequence number: ", tcp.Seq)
		fmt.Println()

	}

	fmt.Println("All packet layers:")
	for _, layer := range packet.Layers() {
		fmt.Println("- ", layer.LayerType())
	}

	applicationLayer := packet.ApplicationLayer()
	if applicationLayer != nil {
		fmt.Println("Application layer/Payload found.")
		fmt.Printf("%s\n", applicationLayer.Payload())

		if strings.Contains(string(applicationLayer.Payload()), "HTTP") {
			fmt.Println("HTTP found!")
		}
	}

	if err := packet.ErrorLayer(); err != nil {
		fmt.Println("Error decoding some part of the packet:", err)
	}
}

func fasterDecoder(packet gopacket.Packet) {
	parser := gopacket.NewDecodingLayerParser(
		layers.LayerTypeEthernet,
		&ethLayer,
		&ipLayer,
		&tcpLayer,
		&tlsLayer,
		&udpLayer,
		&dnsLayer,
	)
	foundLayerTypes := []gopacket.LayerType{}

	err := parser.DecodeLayers(packet.Data(), &foundLayerTypes)
	if err != nil {
		fmt.Println("Trouble decoding layers: ", err)
	}

	for _, layerType := range foundLayerTypes {
		switch layerType {
		case layers.LayerTypeIPv4:
			fmt.Println("IPv4: ", ipLayer.SrcIP, "->", ipLayer.DstIP)
		case layers.LayerTypeTCP:
			fmt.Println("TCP Port: ", tcpLayer.SrcPort, "->", tcpLayer.DstPort)
			fmt.Println("TCP SYN", tcpLayer.SYN, " | ACK:", tcpLayer.ACK)
			if payload := tcpLayer.Payload; len(payload) != 0 {
				reader := bufio.NewReader(bytes.NewReader(payload))
				httpReq, err := http.ReadRequest(reader)
				if err == nil {
					fmt.Println("HTTP Req: ", httpReq)
				}
			}
		case layers.LayerTypeTLS:
			fmt.Println("TLS")
		case layers.LayerTypeUDP:
			fmt.Println("UDP Port: ", udpLayer.SrcPort, "->", udpLayer.DstPort)
		case layers.LayerTypeDNS:
			fmt.Println("DNS opCode: ", strconv.Itoa(int(dnsLayer.OpCode)), "Response Code: ")
			fmt.Print(" Response Code: ", strconv.Itoa(int(dnsLayer.ResponseCode)))
			fmt.Print("\nQuestions:\n")
			for _, dnsQuestions := range dnsLayer.Questions {
				fmt.Println(string(dnsQuestions.Name))
			}
		}
	}
	fmt.Println()
}
