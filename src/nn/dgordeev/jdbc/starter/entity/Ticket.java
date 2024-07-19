package nn.dgordeev.jdbc.starter.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Ticket {

    private Long id;
    private String passengerNo;
    private String passengerName;
    private Flight flight;
    private String seatNo;
    private BigDecimal cost;

    public static TicketBuilder builder() {
        return new TicketBuilder();
    }

    public static class TicketBuilder {

        private final Ticket ticket;

        public TicketBuilder() {
            this.ticket = new Ticket();
        }

        public TicketBuilder id(Long id) {
            this.ticket.setId(id);
            return this;
        }

        public TicketBuilder passengerNo(String passengerNo) {
            this.ticket.setPassengerNo(passengerNo);
            return this;
        }

        public TicketBuilder passengerName(String passengerName) {
            this.ticket.setPassengerName(passengerName);
            return this;
        }

        public TicketBuilder flight(Flight flight) {
            this.ticket.setFlight(flight);
            return this;
        }

        public TicketBuilder seatNo(String seatNo) {
            this.ticket.setSeatNo(seatNo);
            return this;
        }

        public TicketBuilder cost(BigDecimal cost) {
            this.ticket.setCost(cost);
            return this;
        }

        public Ticket build() {
            return this.ticket;
        }
    }
    public Ticket() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Ticket ticket = (Ticket) object;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", passengerNo='" + passengerNo + '\'' +
               ", passengerName='" + passengerName + '\'' +
               ", flight=" + flight +
               ", seatNo='" + seatNo + '\'' +
               ", cost=" + cost +
               '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
