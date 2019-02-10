package ca.llamabagel.transpo.dao

import ca.llamabagel.transpo.dao.impl.GtfsDatabase
import ca.llamabagel.transpo.models.gtfs.Stop
import com.opentable.db.postgres.junit.EmbeddedPostgresRules
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GtfsDatabaseTests {

    @get:Rule
    val pg = EmbeddedPostgresRules.singleInstance()

    @Before
    fun setupDatabase() {
        var connection = pg.embeddedPostgres.postgresDatabase.getConnection("postgres", "postgres")
        connection.prepareStatement("CREATE DATABASE transit").execute()

        connection = pg.embeddedPostgres.getDatabase("postgres", "transit").getConnection("postgres", "postgres")
        connection.prepareStatement("create table stops\n" +
                "(\n" +
                "  id                 varchar(10)      not null\n" +
                "    constraint stops_pk\n" +
                "      primary key,\n" +
                "  code               varchar(10),\n" +
                "  name               text             not null,\n" +
                "  description        text,\n" +
                "  latitude           double precision not null,\n" +
                "  longitude          double precision not null,\n" +
                "  zoneid             integer,\n" +
                "  stopurl            text,\n" +
                "  locationtype       integer,\n" +
                "  parentstation      varchar(10),\n" +
                "  timezone           varchar(10),\n" +
                "  wheelchairboarding integer\n" +
                ")").execute()


        connection.prepareStatement("INSERT INTO stops VALUES ('AA100', '3000', 'Test Stop', null, -45.0, 75.0, 1, null, null, null, null, null)")
                .execute()
    }

    @Test
    fun testStopGet() {
        val source = GtfsDatabase(pg.embeddedPostgres.getDatabase("postgres", "transit").getConnection("postgres", "postgres"))

        val stop = source.stops.getById("AA100")
        assertTrue(stop != null && stop.id == "AA100")
    }

    @Test
    fun testStopInsert() {
        val source = GtfsDatabase(pg.embeddedPostgres.getDatabase("postgres", "transit").getConnection("postgres", "postgres"))

        assertTrue(source.stops.insert(Stop("BB200", "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)))
    }

}