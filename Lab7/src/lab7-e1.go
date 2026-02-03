package main

import (
	"fmt"
	"math/rand"
)

func main() {
	buffer := make(chan int)
	done := make(chan int)

	go producer(buffer)
	go consumer(buffer)

	<-done
}

func producer(buffer chan int) {
	rand.Seed(42)
	for {
		n := rand.Intn(100)
		buffer <- n
	}
}

func consumer(buffer chan int) {
	for {
		n := <-buffer
		if n > 50 {
			fmt.Println(n)
		}
	}
}
