package nn.dgordeev.jdbc.starter;

import nn.dgordeev.jdbc.starter.dao.TicketDao;
import nn.dgordeev.jdbc.starter.dto.TicketFilter;
import nn.dgordeev.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {

    public static void main(String[] args) {
//        testInsertDelete();
//        testSelect();
//        testSelectUpdate();
//        testTicketNotFoundCase();
//        testSelectWithOffsetAndLimit();
    }

    private static void testSelectWithOffsetAndLimit() {
        var ticketDao = TicketDao.getInstance();
        var filter = new TicketFilter(0, 0, 0, null,
                "Иван Иванов", 1, null, null, false);
        var tickets = ticketDao.findAll(filter);
        System.out.println();
        tickets.forEach(System.out::println);
    }

    private static void testTicketNotFoundCase() {
        var ticketDao = TicketDao.getInstance();
        var ticket = ticketDao.findByIdRequired(475L);
        System.out.println(ticket);
    }

    private static void testSelectUpdate() {
        var ticketDao = TicketDao.getInstance();

        var ticket1 = ticketDao.findByIdRequired(51L);
        ticket1.setCost(new BigDecimal("543.2"));
        ticket1.setPassengerName("Александр Расторгуев");

        ticketDao.update(ticket1);
        var updatedTicket = ticketDao.findByIdRequired(ticket1.getId());
        System.out.println(updatedTicket);
    }

    private static void testSelect() {
        var ticketDao = TicketDao.getInstance();
        var ticket1 = ticketDao.findByIdRequired(51L);
        var ticket2 = ticketDao.findById(44L);
        System.out.println(ticket1);
        ticket2.ifPresent(System.out::println);
    }

    private static void testInsertDelete() {
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
