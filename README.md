<h1>Merge Sort</h1>
Before attempting to run the sequential or parallel implementation, please enter the "-Xint" flag within the vm options. This will give the most accurate readings.

<h2>Sequential Mergesort</h2>

1. Open the file "MergeSort".

2. Change `fileName` variable to desired dataset that can be found in the resources package.

3. Run `MergeSort.main()`

<h2>Parallel Mergesort</h2>

1. Open the file "Parellel".

2. Change `MAX_ADDITIONAL_THREADS` variable to desired additional threads. (Note that `main()` runs on a thread on its own. So 1 additional thread equals 2 threads in total.)

3. Change `fileName` variable to desired dataset that can be found in the resources package.

4. Run `Parallel.main()`

<h2>JMS Mergesort</h2>

1. Download and start ActiveMQ

2. Open the file "JMSProducer".

3. Change `CONSUMER_COUNT` variable to desired amount of nodes.

4. Change `fileName` variable to desired dataset that can be found in the resources package.

5. Run `JMSProducer.main()`

<h2>Available Datasets</h2>
There is a set of predefined datasets available for testing. Below are the paths of the files within the project:

`src/main/resources/100k.csv`

`src/main/resources/500k.csv`

`src/main/resources/1mil.csv`

`src/main/resources/4mil.csv`

`src/main/resources/8mil.csv`
