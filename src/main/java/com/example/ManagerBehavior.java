package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command> {

    public interface Command extends Serializable {}

    public static class InstructionCommand implements Command {
        public static final long serialVersionUID = 22L;
        private String message;

        public InstructionCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ResultCommand implements Command {
        public static final long serialVersionUID = 22L;
        private BigInteger result;

        public ResultCommand(BigInteger result) {
            this.result = result;
        }

        public BigInteger getResult() {
            return result;
        }
    }

    private ManagerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ManagerBehavior::new);
    }

    private SortedSet<BigInteger> primes = new TreeSet<>();
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(InstructionCommand.class, command -> {
                    if(command.getMessage().equalsIgnoreCase("start")) {
                        for (int i = 1; i <= 20; i++) {
                            ActorRef<WorkerBehavior.Command> actorRef = getContext().spawn(WorkerBehavior.create(), "Worker-" + i);
                            actorRef.tell(new WorkerBehavior.Command("start", getContext().getSelf()));
                        }
                    }
                    return this;
                })
                .onMessage(ResultCommand.class, command -> {
                    primes.add(command.getResult());
                    System.out.println("Recieved: "+command.getResult());
                    if(primes.size() == 20) {
                        System.out.println("all 20 primes recieved");
                    }
                    return this;
                })
                .build();
    }
}
