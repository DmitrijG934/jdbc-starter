package nn.dgordeev.jdbc.starter.dao;

public class TicketDao {

    private volatile static TicketDao INSTANCE;

    private TicketDao() {}

    public static TicketDao getInstance() {
        if (INSTANCE == null) {
            synchronized (TicketDao.class) {
                if (INSTANCE == null) {
                    return new TicketDao();
                }
            }
        }
        return INSTANCE;
    }
}
