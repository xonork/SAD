package main

import (
	"flag"
	"fmt"
	"log"
	"time"

	"github.com/google/gopacket"
	"github.com/google/gopacket/pcap"
	internal "github.com/xonork/SAD/sniffer/internal/mypacket"

	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/layout"
	"fyne.io/fyne/v2/widget"
)

var (
	device         string = "enp0s3"
	snapshot_len   int32  = 1024
	promiscuous    bool   = false
	err            error
	timeout        time.Duration = 30 * time.Second
	handle         *pcap.Handle
	componentsList = []string{"hola"}
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
	filter := flag.String("f", "", "BPF syntax")
	flag.Parse()

	//Opens the device and returns a handler
	handle, err = pcap.OpenLive(device, snapshot_len, promiscuous, timeout)
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
	d := internal.DecodeLayersVar{}
	//Initializes the channel
	var ch = make(chan internal.Packet)
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
				componentsList = append(componentsList, internal.PacketToString(i))
				componentsTree.Refresh()
				cont.Refresh()
				//fmt.Println("Protocol: " + i.HighestProtocol + " Ports: " + strconv.Itoa(i.SrcPort) + " ---> " + strconv.Itoa(i.DstPort))
			}
		}
	}()

	window.ShowAndRun()

}

func makeTableTab(_ fyne.Window) fyne.CanvasObject {
	t := widget.NewTable(
		func() (int, int) { return 1, 6 },
		func() fyne.CanvasObject {
			return widget.NewLabel("Cell 000, 000")
		},
		func(id widget.TableCellID, cell fyne.CanvasObject) {
			label := cell.(*widget.Label)
			switch id.Col {
			case 0:
				label.SetText(fmt.Sprintf("%d", id.Row+1))
			case 1:
				label.SetText("A longer cell")
			default:
				label.SetText(fmt.Sprintf("Cell %d, %d", id.Row+1, id.Col+1))
			}
		})
	t.SetColumnWidth(0, 34)
	t.SetColumnWidth(1, 102)
	return t
}
