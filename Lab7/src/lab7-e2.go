package main

import (
	"fmt"
	"math/rand"
)

func main() {
	buffer := make(chan int)
	done := make(chan int)

	go producer(buffer)
	go consumer(buffer, done)

	<-done
}

func producer(buffer chan int) {
	rand.Seed(42)
	for i := 0; i < 10000; i++ {
		n := rand.Intn(100)
		buffer <- n
	}

	close(buffer)
}

func consumer(buffer chan int, done chan int) {
	for n := range buffer {
		if n > 50 {
			fmt.Println(n)
		}
	}

	done <- 1
}
