package nn.dgordeev.jdbc.starter.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public record TicketFilter(
        int limit,
        int offset,
        long id,
        String passengerNo,
        String passengerName,
        long flightId,
        String seatNo,
        BigDecimal cost,
        boolean distinctFilter
) {

    private static final long DEFAULT_LIMIT_VALUE = 10;

    public record QueryParameters(String sql, List<Object> prepareParams) {
    }

    public QueryParameters getQueryParameters() {
        List<Object> paramsToPrepare = new ArrayList<>();
        List<String> whereSqlFilters = new ArrayList<>();
        if (this.id() != 0) {
            whereSqlFilters.add("id = ?");
            paramsToPrepare.add(this.id);
        }
        if (this.flightId() != 0) {
            whereSqlFilters.add("flight_id = ?");
            paramsToPrepare.add(this.flightId);
        }
        if (this.passengerNo() != null) {
            whereSqlFilters.add("passenger_no = ?");
            paramsToPrepare.add(this.passengerNo);
        }
        if (this.passengerName() != null) {
            whereSqlFilters.add("passenger_name = ?");
            paramsToPrepare.add(this.passengerName);
        }
        if (this.seatNo() != null) {
            whereSqlFilters.add("seat_no = ?");
            paramsToPrepare.add(this.seatNo);
        }
        if (this.cost() != null) {
            whereSqlFilters.add("cost = ?");
            paramsToPrepare.add(this.cost);
        }
        paramsToPrepare.add(this.limit != 0 ? this.limit : DEFAULT_LIMIT_VALUE);
        paramsToPrepare.add(this.offset);
        var sql = whereSqlFilters
                .stream()
                .collect(
                        joining(
                                " AND ",
                                whereSqlFilters.isEmpty() ? "" : " WHERE ",
                                " LIMIT ? OFFSET ?")
                );
        return new QueryParameters(sql, paramsToPrepare);
    }
}
