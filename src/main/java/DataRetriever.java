import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private final DBConnection dbConnection = new DBConnection();

    //Q1
    public long countAllVotes() {
        String sql = "SELECT COUNT(*) AS total_votes FROM vote";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong("total_votes");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des votes", e);
        }

        return 0L;
    }

    //Q2
    public List<VoteTypeCount> countVotesByType() {
        String sql = """
                SELECT vote_type, COUNT(*) AS count
                FROM vote
                GROUP BY vote_type
                ORDER BY 
                    CASE vote_type
                        WHEN 'VALID' THEN 1
                        WHEN 'BLANK' THEN 2
                        WHEN 'NULL' THEN 3
                    END
                """;

        List<VoteTypeCount> results = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new VoteTypeCount(
                        VoteType.valueOf(rs.getString("vote_type")),
                        rs.getLong("count")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des votes par type", e);
        }

        return results;
    }
}