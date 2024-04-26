package com.graphqljava.tutorial.retail.controllers;

import java.util.List;

import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import com.graphqljava.tutorial.retail.models.ChinookModels.Album;
import com.graphqljava.tutorial.retail.models.ChinookModels.Artist;
import com.graphqljava.tutorial.retail.models.ChinookModels.Customer;
import com.graphqljava.tutorial.retail.models.ChinookModels.Employee;
import com.graphqljava.tutorial.retail.models.ChinookModels.Genre;
import com.graphqljava.tutorial.retail.models.ChinookModels.Invoice;
import com.graphqljava.tutorial.retail.models.ChinookModels.InvoiceLine;
import com.graphqljava.tutorial.retail.models.ChinookModels.MediaType;
import com.graphqljava.tutorial.retail.models.ChinookModels.Playlist;
import com.graphqljava.tutorial.retail.models.ChinookModels.PlaylistTrack;
import com.graphqljava.tutorial.retail.models.ChinookModels.Track;

public class ChinookControllers {
    @Controller
    public static class AlbumController {
        private final JdbcClient jdbcClient;

        private final RowMapper<Album>
                albumMapper = (rs, rowNum) -> new Album(
                rs.getInt("AlbumId"),
                rs.getString("Title"),
                rs.getInt("ArtistId")
        );

        public AlbumController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Album>
        Album(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Album\"") :
                    jdbcClient.sql("select * from \"Album\" limit ?").param(limit.value());
            return
                    spec
                            .query(albumMapper)
                            .list();
        }

        @QueryMapping
        List<Album>
        Album_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Album\" where AlbumId = ?").param(id)
                    .query(albumMapper)
                    .list();
        }
    }

    @Controller
    public static class ArtistController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Artist>
                artistMapper = (rs, rowNum) -> new Artist(rs.getInt("ArtistId"),
                rs.getString("Name"));

        public ArtistController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Artist>
        Artist(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Artist\"") :
                    jdbcClient.sql("select * from \"Artist\" limit ?").param(limit.value());
            return
                    spec
                            .query(artistMapper)
                            .list();
        }

        @QueryMapping
        List<Artist>
        Artist_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Artist\" where ArtistId = ?").param(id)
                    .query(artistMapper)
                    .list();
        }
    }

    @Controller
    public static class CustomerController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Customer>
                customerMapper = (rs, rowNum) -> new Customer(
                rs.getInt("CustomerId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Company"),
                rs.getString("Address"),
                rs.getString("City"),
                rs.getString("State"),
                rs.getString("Country"),
                rs.getString("PostalCode"),
                rs.getString("Phone"),
                rs.getString("Fax"),
                rs.getString("Email"),
                rs.getInt("SupportRepId")
        );

        public CustomerController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Customer>
        Customer(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Customer\"") :
                    jdbcClient.sql("select * from \"Customer\" limit ?").param(limit.value());
            return
                    spec
                            .query(customerMapper)
                            .list();
        }

        @QueryMapping
        List<Customer>
        Customer_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Customer\" where CustomerId = ?").param(id)
                    .query(customerMapper)
                    .list();
        }
    }

    @Controller
    public static class EmployeeController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Employee>
                employeeMapper = (rs, rowNum) -> new Employee(
                rs.getInt("EmployeeId"),
                rs.getString("LastName"),
                rs.getString("FirstName"),
                rs.getString("Title"),
                rs.getInt("ReportsTo"),
                rs.getString("BirthDate"),
                rs.getString("HireDate"),
                rs.getString("Address"),
                rs.getString("City"),
                rs.getString("State"),
                rs.getString("Country"),
                rs.getString("PostalCode"),
                rs.getString("Phone"),
                rs.getString("Fax"),
                rs.getString("Email")
        );

        public EmployeeController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Employee>
        Employee(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Employee\"") :
                    jdbcClient.sql("select * from \"Employee\" limit ?").param(limit.value());
            return
                    spec
                            .query(employeeMapper)
                            .list();
        }

        @QueryMapping
        List<Employee>
        Employee_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Employee\" where EmployeeId = ?").param(id)
                    .query(employeeMapper)
                    .list();
        }
    }

    @Controller
    public static class GenreController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Genre>
                genreMapper = (rs, rowNum) -> new Genre(
                rs.getInt("GenreId"),
                rs.getString("Name")
        );

        public GenreController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Genre>
        Genre(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Genre\"") :
                    jdbcClient.sql("select * from \"Genre\" limit ?").param(limit.value());
            return
                    spec
                            .query(genreMapper)
                            .list();
        }

        @QueryMapping
        List<Genre>
        Genre_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Genre\" where GenreId = ?").param(id)
                    .query(genreMapper)
                    .list();
        }
    }

    @Controller
    public static class InvoiceController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Invoice>
                invoiceMapper = (rs, rowNum) -> new Invoice(
                rs.getInt("InvoiceId"),
                rs.getInt("CustomerId"),
                rs.getString("InvoiceDate"),
                rs.getString("BillingAddress"),
                rs.getString("BillingCity"),
                rs.getString("BillingState"),
                rs.getString("BillingCountry"),
                rs.getString("BillingPostalCode"),
                rs.getFloat("Total")
        );

        public InvoiceController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Invoice>
        Invoice(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Invoice\"") :
                    jdbcClient.sql("select * from \"Invoice\" limit ?").param(limit.value());
            return
                    spec
                            .query(invoiceMapper)
                            .list();
        }

        @QueryMapping
        List<Invoice>
        Invoice_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Invoice\" where InvoiceId = ?").param(id)
                    .query(invoiceMapper)
                    .list();
        }
    }

    @Controller
    public static class InvoiceLineController {
        private final JdbcClient jdbcClient;
        private final RowMapper<InvoiceLine>
                invoiceLineMapper = (rs, rowNum) -> new InvoiceLine(
                rs.getInt("InvoiceLineId"),
                rs.getInt("InvoiceId"),
                rs.getInt("TrackId"),
                rs.getFloat("UnitPrice"),
                rs.getInt("Quantity")
        );

        public InvoiceLineController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<InvoiceLine>
        InvoiceLine(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"InvoiceLine\"") :
                    jdbcClient.sql("select * from \"InvoiceLine\" limit ?").param(limit.value());
            return
                    spec
                            .query(invoiceLineMapper)
                            .list();
        }

        @QueryMapping
        List<InvoiceLine>
        InvoiceLine_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"InvoiceLine\" where InvoiceLineId = ?").param(id)
                    .query(invoiceLineMapper)
                    .list();
        }
    }

    @Controller
    public static class MediaTypeController {
        private final JdbcClient jdbcClient;
        private final RowMapper<MediaType>
                mediaTypeMapper = (rs, rowNum) -> new MediaType(
                rs.getInt("MediaTypeId"),
                rs.getString("Name")
        );

        public MediaTypeController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<MediaType>
        MediaType(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"MediaType\"") :
                    jdbcClient.sql("select * from \"MediaType\" limit ?").param(limit.value());
            return
                    spec
                            .query(mediaTypeMapper)
                            .list();
        }

        @QueryMapping
        List<MediaType>
        MediaType_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"MediaType\" where MediaTypeId = ?").param(id)
                    .query(mediaTypeMapper)
                    .list();
        }
    }

    @Controller
    public static class PlaylistController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Playlist>
                playlistMapper = (rs, rowNum) -> new Playlist(
                rs.getInt("PlaylistId"),
                rs.getString("Name")
        );

        public PlaylistController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Playlist>
        Playlist(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Playlist\"") :
                    jdbcClient.sql("select * from \"Playlist\" limit ?").param(limit.value());
            return
                    spec
                            .query(playlistMapper)
                            .list();
        }

        @QueryMapping
        List<Playlist>
        Playlist_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Playlist\" where PlaylistId = ?").param(id)
                    .query(playlistMapper)
                    .list();
        }
    }

    @Controller
    public static class PlaylistTrackController {
        private final JdbcClient jdbcClient;
        private final RowMapper<PlaylistTrack>
                playlistTrackMapper = (rs, rowNum) -> new PlaylistTrack(
                rs.getInt("PlaylistId"),
                rs.getInt("TrackId")
        );

        public PlaylistTrackController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<PlaylistTrack>
        PlaylistTrack(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"PlaylistTrack\"") :
                    jdbcClient.sql("select * from \"PlaylistTrack\" limit ?").param(limit.value());
            return
                    spec
                            .query(playlistTrackMapper)
                            .list();
        }

        @QueryMapping
        List<PlaylistTrack>
        PlaylistTrack_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"PlaylistTrack\" where PlaylistTrackId = ?").param(id)
                    .query(playlistTrackMapper)
                    .list();
        }
    }

    @Controller
    public static class TrackController {
        private final JdbcClient jdbcClient;
        private final RowMapper<Track>
                trackMapper = (rs, rowNum) -> new Track(
                rs.getInt("TrackId"),
                rs.getString("Name"),
                rs.getInt("AlbumId"),
                rs.getInt("GenreId"),
                rs.getInt("MediaTypeId"),
                rs.getString("Composer"),
                rs.getInt("Milliseconds"),
                rs.getInt("Bytes"),
                rs.getFloat("UnitPrice")
        );

        public TrackController(JdbcClient jdbcClient) {
            this.jdbcClient = jdbcClient;
        }

        @QueryMapping
        List<Track>
        Track(@Argument ArgumentValue<Integer> limit) {
            StatementSpec
                    spec = limit.isOmitted() ?
                    jdbcClient.sql("select * from \"Track\"") :
                    jdbcClient.sql("select * from \"Track\" limit ?").param(limit.value());
            return
                    spec
                            .query(trackMapper)
                            .list();
        }

        @QueryMapping
        List<Track>
        Track_by_pk(@Argument int id) {
            return jdbcClient.sql("select * from \"Track\" where TrackId = ?").param(id)
                    .query(trackMapper)
                    .list();
        }
    }
}
