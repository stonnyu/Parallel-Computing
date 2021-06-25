package jms;

import Utils.PrivateMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import sort.ReadCSV;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JMSProducer {

    private final static int CONSUMER_COUNT = 8;

    public static void main(String[] args) {
        // Read values
        String fileName = "src/main/resources/100k.csv";
        Double[] unsortedArray = ReadCSV.readCSV(fileName);

        long startTime = System.currentTimeMillis();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JMSMagic.SERVER_CONNECTION);
        connectionFactory.setTrustAllPackages(true);

        try {
            // Connect to ActiveMQ
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Prepare queues
            Queue queue = session.createQueue(JMSMagic.UNSORTED_QUEUE_NAME);
            Queue sortedQueue = session.createQueue(JMSMagic.SORTED_QUEUE_NAME);

            // Prepare entities
            MessageProducer messageProducer = session.createProducer(queue);
            MessageConsumer messageConsumer = session.createConsumer(sortedQueue);

            connection.start();

            // launch consumers
            JMSConsumer.launchConsumers(unsortedArray.length, CONSUMER_COUNT);

            // Max length of individual arrays
            int maxLength = unsortedArray.length / (CONSUMER_COUNT * 2);

            // Split array into number of arrays of consumer count
            List<Double[]> unsortedArrays = splitArray(unsortedArray, maxLength);

            // Prepare message from array for each consumer
            unsortedArrays.forEach((array) -> {
                StringBuilder stringBuilder = DataConverter.stringComposer(array);

                // Send messages to queue
                try {
                    ObjectMessage message = session.createObjectMessage(new PrivateMessage(stringBuilder.toString(), 0));
                    messageProducer.send(message);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            // Listen to sorted queue
            MessageListener listener = message -> {
                try {
                    PrivateMessage textMessage = (PrivateMessage) ((ObjectMessage) message).getObject();
                    Double[] array = DataConverter.stringDecomposer(textMessage.getMessage());

                    long endTime = System.currentTimeMillis();
                    System.out.println("genius sort = total sorting time in ms:" + (endTime - startTime));

                    // Printing sorted array
//                    for (int i = 0; i < array.length; i++) {
//                        System.out.println(array[i]);
//                    }

                } catch (JMSException e) {
                    e.printStackTrace();
                }

            };
            messageConsumer.setMessageListener(listener);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static List<Double[]> splitArray(Double[] initialArray, int maxLength) {
        List<Double[]> result = new ArrayList<>();
        if (initialArray == null || initialArray.length == 0) {
            return result;
        }

        int from = 0;
        int to = 0;
        int slicedItems = 0;
        while (slicedItems < initialArray.length) {
            to = from + Math.min(maxLength, initialArray.length - to);
            Double[] slice = Arrays.copyOfRange(initialArray, from, to);
            result.add(slice);
            slicedItems += slice.length;
            from = to;
        }
        return result;
    }
}
