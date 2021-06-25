package jms;

import Utils.PrivateMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import sort.MergeSort;

import javax.jms.*;
import java.util.ArrayList;

public class JMSConsumer {
    public static void consumer(int i, int unsortedDataLength) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(JMSMagic.SERVER_CONNECTION);
        // From versions 5.12.2 and 5.13.1 ActiveMQ does not trust the content of ObjectMessages. The following
        // line is a dirty trick to let ActiveMQ trust the content. In real life one should specify the package(s)
        // that can be trusted. See: https://activemq.apache.org/objectmessage.html
        connectionFactory.setTrustAllPackages(true);
        try {
            // Connect to ActiveMQ
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Prepare queues
            Queue queue = session.createQueue(JMSMagic.UNSORTED_QUEUE_NAME);
            Queue sortedQueue = session.createQueue(JMSMagic.SORTED_QUEUE_NAME);

            // Prepare entities
            MessageConsumer messageConsumer = session.createConsumer(queue);
            MessageProducer messageProducer = session.createProducer(queue);

            connection.start();

            ArrayList<Message> messages = new ArrayList<>();

            var ref = new Object() {
                int id = i;
            };

            // Listen to unsorted queue
            MessageListener listener = message -> {
                messages.add(message);
                if (messages.size() == 2) {
                    StringBuilder sb = new StringBuilder();
                    messages.forEach(message1 -> {
                        try {
                            PrivateMessage textMessage = (PrivateMessage) ((ObjectMessage) message1).getObject();
                            sb.append(textMessage.getMessage()).append(",");
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    });

                    // Array gets sorted
                    Double[] array = DataConverter.stringDecomposer(sb.toString());
                    MergeSort.mergeSort(array);

                    // Clear received messages from queue
                    messages.clear();

                    // Prepare message for sending
                    ObjectMessage returnMessage;
                    try {
                        StringBuilder stringBuilder = DataConverter.stringComposer(array);

                        // Send to sorted queue
                        if (array.length == unsortedDataLength) {
                            MessageProducer finalMessageProducer = session.createProducer(sortedQueue);
                            returnMessage = session.createObjectMessage(new PrivateMessage(stringBuilder.toString(), 0));
                            finalMessageProducer.send(returnMessage);
                            connection.close();
                        } else {
                            // Send to unsorted queue
                            returnMessage = session.createObjectMessage(new PrivateMessage(stringBuilder.toString(), 0));
                            messageProducer.send(returnMessage);

                            // Close half the (odd) consumers because they're not needed
                            if (ref.id % 2 == 1) {
                                connection.close();
                            } else if (ref.id != 0 && ref.id % 2 == 0) {
                                ref.id = ref.id / 2;
                            }
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };

            messageConsumer.setMessageListener(listener);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void launchConsumers(int unsortedDataLength, int consumerCount) {
        for (int i = 0; i < consumerCount; i++) {
            JMSConsumer.consumer(i, unsortedDataLength);
        }
    }
}

