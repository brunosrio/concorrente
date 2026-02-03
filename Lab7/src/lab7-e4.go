package main

import (
	"fmt"
	"math/rand"
)

func main() {
	buffer := make(chan int, 100)
	join := make(chan int)
	doneProducer1 := make(chan int)

	go producer1(buffer, doneProducer1)
	go producer2(buffer, doneProducer1)
	go consumer(buffer, join)

	<-join
}

func producer1(buffer chan<- int, doneProducer1 chan<- int) {
	rand.Seed(42)
	limite := rand.Intn(100)

	for i := 0; i < limite; i++ {
		n := rand.Intn(100)
		buffer <- n
	}

	doneProducer1 <- 1
}

func producer2(buffer chan<- int, doneProducer1 <-chan int) {
	rand.Seed(42)
	limite := rand.Intn(100)

	for i := 0; i < limite; i++ {
		n := rand.Intn(100)
		buffer <- n
	}

	<-doneProducer1
	close(buffer)
}

func consumer(buffer <-chan int, join chan int) {
	for n := range buffer {
		if n > 50 {
			fmt.Println(n)
		}
	}

	join <- 1
}
