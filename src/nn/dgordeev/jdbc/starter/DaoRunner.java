package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.dao.TicketDao;
import nn.dgordeev.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {

    public static void main(String[] args) {
        var ticketDao = TicketDao.getInstance();
        var ticket = Ticket.builder()
                .flightId(9L)
                .cost(BigDecimal.TEN)
                .seatNo("C1")
                .passengerName("Alex Wilson")
                .passengerNo("66754")
                .build();

        var savedTicket = ticketDao.save(ticket);
        System.out.println(savedTicket);

        ticketDao.delete(savedTicket.getId());

    }
}
