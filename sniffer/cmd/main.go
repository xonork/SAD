package main

import (
	"flag"
	"fmt"
	"log"
	"strconv"
	"time"

	internal "github.com/xonork/SAD/sniffer/internal/mypacket"

	"github.com/google/gopacket"
	"github.com/google/gopacket/pcap"
)

var (
	device       string = "enp0s3"
	snapshot_len int32  = 1024
	promiscuous  bool   = false
	err          error
	timeout      time.Duration = 30 * time.Second
	handle       *pcap.Handle
)

func main() {
	filter := flag.String("f", "", "BPF syntax")
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
	d := internal.DecodeLayersVar{}
	var ch = make(chan internal.Packet)
	packetSource := gopacket.NewPacketSource(handle, handle.LinkType())
	go func(packetSource *gopacket.PacketSource) {
		for packet := range packetSource.Packets() {
			go d.FasterDecoder(packet, ch)
		}
	}(packetSource)
	for {
		if i, ok := <-ch; ok {
			fmt.Println("Protocol: " + i.HighestProtocol + " Ports: " + strconv.Itoa(i.SrcPort) + " ---> " + strconv.Itoa(i.DstPort))
		}
	}

}
