package com.example.askpattern;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command> {

    public static class Command implements Serializable {
        private static final long serialVersionUID = 123L;
        private String message;
        private ActorRef<ManagerBehavior.Command> sender;

        public Command(String message, ActorRef<ManagerBehavior.Command> sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public ActorRef<ManagerBehavior.Command> getSender() {
            return sender;
        }
    }

    private WorkerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(WorkerBehavior::new);
    }

//    private BigInteger prime;
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(command -> {
                    BigInteger prime = null;
                    if(command.getMessage().equalsIgnoreCase("start")) {
                        prime = new BigInteger(2000, new Random());
                        prime = prime.nextProbablePrime();
                        Random r = new Random();
                        if (r.nextInt(5) < 2) {
                            command.getSender().tell(new ManagerBehavior.ResultCommand(prime));
                        }
                    }
                    return subsequentHandler(prime);
                })
                .build();
    }

    public Receive<Command> subsequentHandler(BigInteger prime) {
        return newReceiveBuilder()
                .onAnyMessage(command -> {
                    if(command.getMessage().equalsIgnoreCase("start")) {
//                        this.prime = new BigInteger(2000, new Random());
                        Random r = new Random();
                        if (r.nextInt(5) < 2) {
                            command.getSender().tell(new ManagerBehavior.ResultCommand(prime));
                        }
//                        command.getSender().tell(new ManagerBehavior.ResultCommand(prime));
                    }
                    return Behaviors.same();
                })
                .build();
    }
}
