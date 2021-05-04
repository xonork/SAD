package main

import (
	"flag"
	"log"
	"time"

	//Sniffer libraries
	"github.com/google/gopacket"
	"github.com/google/gopacket/pcap"

	//Internal libraries
	"github.com/xonork/SAD/sniffer/internal/mypacket"

	//GUI libraries
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/layout"
	"fyne.io/fyne/v2/widget"
)

var (
	snapshot_len   int32 = 1024
	promiscuous    bool  = false
	err            error
	timeout        time.Duration = 30 * time.Second
	handle         *pcap.Handle
	componentsList = []string{}
)

func main() {
	//Creates an app
	app := app.New()
	window := app.NewWindow("Sniffer")

	//Creates List widget
	componentsTree := widget.NewList(
		func() int {
			return len(componentsList)
		},
		func() fyne.CanvasObject {
			return widget.NewLabel("template")
		},
		func(i widget.ListItemID, o fyne.CanvasObject) {
			o.(*widget.Label).SetText(componentsList[i])
		})

	//Adds the List to the window
	cont := container.NewScroll(componentsTree)
	layout := container.New(layout.NewMaxLayout(), cont)
	window.SetContent(layout)
	window.Resize(fyne.NewSize(800, 600))

	//Flags
	var device *string = flag.String("d", "", "Network Interface")
	filter := flag.String("f", "", "BPF syntax")
	flag.Parse()

	//Opens the device and returns a handler
	handle, err = pcap.OpenLive(*device, snapshot_len, promiscuous, timeout)
	if err != nil {
		log.Fatal(err)
	}
	defer handle.Close()

	//Sets the filters
	err = handle.SetBPFFilter(*filter)
	if err != nil {
		log.Fatal(err)
	}

	//Initializes the decoder
	d := mypacket.DecodeLayersVar{}
	//Initializes the channel
	var ch = make(chan mypacket.Packet)
	//Starts sniffing
	packetSource := gopacket.NewPacketSource(handle, handle.LinkType())

	//Starts a thread, which will take the sniffed packages
	go func(packetSource *gopacket.PacketSource) {
		for packet := range packetSource.Packets() {
			/*
				For each packet sniffed, a thread is created,
				it will decode it, and send it through the channel
			*/
			go d.FasterDecoder(packet, ch)
		}
	}(packetSource)

	//Another threat is created with the aim of update the List
	go func() {
		for {
			if i, ok := <-ch; ok {
				componentsList = append(componentsList, mypacket.PacketToString(i))
				componentsTree.Refresh()
				cont.Refresh()
			}
		}
	}()

	window.ShowAndRun()

}
