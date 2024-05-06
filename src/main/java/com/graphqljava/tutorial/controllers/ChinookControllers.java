package com.graphqljava.tutorial.controllers;

import com.graphqljava.tutorial.models.ChinookModels;
import com.graphqljava.tutorial.models.input.AlbumInput;
import com.graphqljava.tutorial.models.input.ArtistInput;
import com.graphqljava.tutorial.models.input.CustomerInput;
import com.graphqljava.tutorial.models.input.EmployeeInput;
import com.graphqljava.tutorial.models.input.GenreInput;
import com.graphqljava.tutorial.models.input.InvoiceInput;
import com.graphqljava.tutorial.models.input.PlaylistInput;
import com.graphqljava.tutorial.models.input.PlaylistTrackInput;
import com.graphqljava.tutorial.models.input.TrackInput;

import com.graphqljava.tutorial.models.input.InvoiceLineInput;
import com.graphqljava.tutorial.models.input.MediaTypeInput;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;


public class ChinookControllers {
    @Controller
    public static class AlbumController extends BaseController {

        public AlbumController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Album\"";
        }

        @QueryMapping
        public List<ChinookModels.Album> Albums(@Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }
            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.Album> Albums(final ChinookModels.Artist artist, @Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }
            if (null == input.getAlbumId()) {
                input.setAlbumId(artist.ArtistId());
            }
            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Album Album(final ChinookModels.Track track, @Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }
            if (null == input.getAlbumId()) {
                input.setAlbumId(track.AlbumId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Album>
                rowMapper = (rs, rowNum) -> new ChinookModels.Album(
                rs.getInt("AlbumId"),
                rs.getString("Title"),
                rs.getInt("ArtistId")
        );

        private StatementSpec spec(final AlbumInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "AlbumId", input.getAlbumId());
            extractInputParameterAndValue(columns, params, "Title", input.getTitle());
            extractInputParameterAndValue(columns, params, "ArtistId", input.getArtistId());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class ArtistController extends BaseController {

        public ArtistController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Artist\"";
        }

        @QueryMapping
        public List<ChinookModels.Artist> Artists(@Argument ArtistInput input) {
            if (null == input) {
                input = new ArtistInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Artist Artist(final ChinookModels.Album album, @Argument ArtistInput input) {
            if (null == input) {
                input = new ArtistInput();
            }
            if (null == input.getArtistId()) {
                input.setArtistId(album.ArtistId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Artist>
                rowMapper = (rs, rowNum) -> new ChinookModels.Artist(
                rs.getInt("ArtistId"),
                rs.getString("Name")
        );

        private StatementSpec spec(final ArtistInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "ArtistId", input.getArtistId());
            extractInputParameterAndValue(columns, params, "Name", input.getName());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class CustomerController extends BaseController {

        public CustomerController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Customer\"";
        }

        @QueryMapping
        public List<ChinookModels.Customer> Customers(@Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Customer Customer(final ChinookModels.Invoice invoice, @Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }
            if (null == input.getCustomerId()) {
                input.setCustomerId(invoice.CustomerId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        @SchemaMapping
        public List<ChinookModels.Customer> Customers(final ChinookModels.Employee employee, @Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }
            if (null == input.getCustomerId()) {
                input.setCustomerId(employee.EmployeeId());
            }

            return spec(input).query(rowMapper).list();
        }

        private static final RowMapper<ChinookModels.Customer>
                rowMapper = (rs, rowNum) -> new ChinookModels.Customer(
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

        private StatementSpec spec(final CustomerInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "CustomerId", input.getCustomerId());
            extractInputParameterAndValue(columns, params, "LastName", input.getLastName());
            extractInputParameterAndValue(columns, params, "FirstName", input.getFirstName());
            extractInputParameterAndValue(columns, params, "Company", input.getCompany());
            extractInputParameterAndValue(columns, params, "Address", input.getAddress());
            extractInputParameterAndValue(columns, params, "City", input.getCity());
            extractInputParameterAndValue(columns, params, "State", input.getState());
            extractInputParameterAndValue(columns, params, "Country", input.getCountry());
            extractInputParameterAndValue(columns, params, "PostalCode", input.getPostalCode());
            extractInputParameterAndValue(columns, params, "Phone", input.getPhone());
            extractInputParameterAndValue(columns, params, "Fax", input.getFax());
            extractInputParameterAndValue(columns, params, "Email", input.getEmail());
            extractInputParameterAndValue(columns, params, "SupportRepId", input.getSupportRepId());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class EmployeeController extends BaseController {

        public EmployeeController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Employee\"";
        }

        @QueryMapping
        public List<ChinookModels.Employee> Employees(@Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Employee SupportRep(final ChinookModels.Customer customer, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getEmployeeId()) {
                input.setEmployeeId(customer.SupportRepId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        @SchemaMapping
        public ChinookModels.Employee Manager(final ChinookModels.Employee employee, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getEmployeeId()) {
                input.setEmployeeId(employee.ReportsTo());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        @SchemaMapping
        public ChinookModels.Employee Reports(final ChinookModels.Employee employee, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getReportsTo()) {
                input.setReportsTo(employee.ReportsTo());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Employee>
                rowMapper = (rs, rowNum) -> new ChinookModels.Employee(
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

        private StatementSpec spec(final EmployeeInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "EmployeeId", input.getEmployeeId());
            extractInputParameterAndValue(columns, params, "LastName", input.getLastName());
            extractInputParameterAndValue(columns, params, "FirstName", input.getFirstName());
            extractInputParameterAndValue(columns, params, "Title", input.getTitle());
            extractInputParameterAndValue(columns, params, "ReportsTo", input.getReportsTo());
            extractInputParameterAndValue(columns, params, "BirthDate", input.getBirthDate());
            extractInputParameterAndValue(columns, params, "HireDate", input.getHireDate());
            extractInputParameterAndValue(columns, params, "Address", input.getAddress());
            extractInputParameterAndValue(columns, params, "City", input.getCity());
            extractInputParameterAndValue(columns, params, "State", input.getState());
            extractInputParameterAndValue(columns, params, "Country", input.getCountry());
            extractInputParameterAndValue(columns, params, "PostalCode", input.getPostalCode());
            extractInputParameterAndValue(columns, params, "Phone", input.getPhone());
            extractInputParameterAndValue(columns, params, "Fax", input.getFax());
            extractInputParameterAndValue(columns, params, "Email", input.getEmail());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class GenreController extends BaseController {

        public GenreController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Genre\"";
        }

        @QueryMapping
        public List<ChinookModels.Genre> Genres(@Argument GenreInput input) {
            if (null == input) {
                input = new GenreInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Genre Genre(final ChinookModels.Track track, @Argument GenreInput input) {
            if (null == input) {
                input = new GenreInput();
            }
            if (null == input.getGenreId()) {
                input.setGenreId(track.GenreId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Genre>
                rowMapper = (rs, rowNum) -> new ChinookModels.Genre(
                rs.getInt("GenreId"),
                rs.getString("Name")
        );

        private StatementSpec spec(final GenreInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "GenreId", input.getGenreId());
            extractInputParameterAndValue(columns, params, "Name", input.getName());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class InvoiceController extends BaseController {

        public InvoiceController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Invoice\"";
        }

        @QueryMapping
        public List<ChinookModels.Invoice> Invoices(@Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.Invoice> Invoices(final ChinookModels.Customer customer, @Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }
            if (null == input.getCustomerId()) {
                input.setCustomerId(customer.CustomerId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Invoice Invoice(final ChinookModels.InvoiceLine invoiceLine, @Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }
            if (null == input.getInvoiceId()) {
                input.setInvoiceId(invoiceLine.InvoiceId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Invoice>
                rowMapper = (rs, rowNum) -> new ChinookModels.Invoice(
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

        private StatementSpec spec(final InvoiceInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "InvoiceId", input.getInvoiceId());
            extractInputParameterAndValue(columns, params, "CustomerId", input.getCustomerId());
            extractInputParameterAndValue(columns, params, "InvoiceDate", input.getInvoiceDate());
            extractInputParameterAndValue(columns, params, "BillingAddress", input.getBillingAddress());
            extractInputParameterAndValue(columns, params, "BillingCity", input.getBillingCity());
            extractInputParameterAndValue(columns, params, "BillingState", input.getBillingState());
            extractInputParameterAndValue(columns, params, "BillingCountry", input.getBillingCountry());
            extractInputParameterAndValue(columns, params, "BillingPostalCode", input.getBillingPostalCode());
            extractInputParameterAndValue(columns, params, "Total", input.getTotal());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class InvoiceLineController extends BaseController {

        public InvoiceLineController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"InvoiceLine\"";
        }

        @QueryMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(@Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(final ChinookModels.Invoice invoice, @Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }
            if (null == input.getInvoiceLineId()) {
                input.setInvoiceId(invoice.InvoiceId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(final ChinookModels.Track track, @Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(track.TrackId());
            }

            return spec(input).query(rowMapper).list();
        }

        private static final RowMapper<ChinookModels.InvoiceLine>
                rowMapper = (rs, rowNum) -> new ChinookModels.InvoiceLine(
                rs.getInt("InvoiceLineId"),
                rs.getInt("InvoiceId"),
                rs.getInt("TrackId"),
                rs.getFloat("UnitPrice"),
                rs.getInt("Quantity")
        );

        private StatementSpec spec(final InvoiceLineInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "InvoiceLineId", input.getInvoiceLineId());
            extractInputParameterAndValue(columns, params, "InvoiceId", input.getInvoiceId());
            extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());
            extractInputParameterAndValue(columns, params, "UnitPrice", input.getUnitPrice());
            extractInputParameterAndValue(columns, params, "Quantity", input.getQuantity());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class MediaTypeController extends BaseController {

        public MediaTypeController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"MediaType\"";
        }

        @QueryMapping
        public List<ChinookModels.MediaType> MediaTypes(@Argument MediaTypeInput input) {
            if (null == input) {
                input = new MediaTypeInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.MediaType MediaType(final ChinookModels.Track track, @Argument MediaTypeInput input) {
            if (null == input) {
                input = new MediaTypeInput();
            }
            if (null == input.getMediaTypeId()) {
                input.setMediaTypeId(track.MediaTypeId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.MediaType>
                rowMapper = (rs, rowNum) -> new ChinookModels.MediaType(
                rs.getInt("MediaTypeId"),
                rs.getString("Name")
        );

        private StatementSpec spec(final MediaTypeInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "MediaTypeId", input.getMediaTypeId());
            extractInputParameterAndValue(columns, params, "Name", input.getName());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class PlaylistController extends BaseController {

        public PlaylistController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Playlist\"";
        }

        @QueryMapping
        public List<ChinookModels.Playlist> Playlists(@Argument PlaylistInput input) {
            if (null == input) {
                input = new PlaylistInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Playlist Playlist(final ChinookModels.PlaylistTrack playlistTrack, @Argument PlaylistInput input) {
            if (null == input) {
                input = new PlaylistInput();
            }
            if (null == input.getPlaylistId()) {
                input.setPlaylistId(playlistTrack.PlaylistId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Playlist>
                rowMapper = (rs, rowNum) -> new ChinookModels.Playlist(
                rs.getInt("PlaylistId"),
                rs.getString("Name")
        );

        private StatementSpec spec(final PlaylistInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "PlaylistId", input.getPlaylistId());
            extractInputParameterAndValue(columns, params, "Name", input.getName());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class PlaylistTrackController extends BaseController {

        public PlaylistTrackController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"PlaylistTrack\"";
        }

        @QueryMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(@Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(final ChinookModels.Playlist playlist, @Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }
            if (null == input.getPlaylistId()) {
                input.setPlaylistId(playlist.PlaylistId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(final ChinookModels.Track track, @Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(track.TrackId());
            }

            return spec(input).query(rowMapper).list();
        }

        private static final RowMapper<ChinookModels.PlaylistTrack>
                rowMapper = (rs, rowNum) -> new ChinookModels.PlaylistTrack(
                rs.getInt("PlaylistId"),
                rs.getInt("TrackId")
        );

        private StatementSpec spec(final PlaylistTrackInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "PlaylistId", input.getPlaylistId());
            extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

    @Controller
    public static class TrackController extends BaseController {

        public TrackController(final JdbcClient jdbcClient) {
            super(jdbcClient);
        }

        @Override
        public String getTablePrefix() {
            return "\"Track\"";
        }

        @QueryMapping
        public List<ChinookModels.Track> Tracks(@Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.Album album, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getAlbumId()) {
                input.setAlbumId(album.AlbumId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.Genre genre, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getGenreId()) {
                input.setGenreId(genre.GenreId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Track Track(final ChinookModels.InvoiceLine invoiceLine, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(invoiceLine.TrackId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.MediaType mediaType, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getMediaTypeId()) {
                input.setMediaTypeId(mediaType.MediaTypeId());
            }

            return spec(input).query(rowMapper).list();
        }

        @SchemaMapping
        public ChinookModels.Track Track(final ChinookModels.PlaylistTrack playlistTrack, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(playlistTrack.TrackId());
            }

            return spec(input).query(rowMapper).optional().orElse(null);
        }

        private static final RowMapper<ChinookModels.Track>
                rowMapper = (rs, rowNum) -> new ChinookModels.Track(
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

        protected StatementSpec spec(final TrackInput input) {
            List<String> columns = new ArrayList<>();
            List<Object> params = new ArrayList<>();

            extractInputParameterAndValue(columns, params, "TrackId", input.getTrackId());
            extractInputParameterAndValue(columns, params, "Name", input.getName());
            extractInputParameterAndValue(columns, params, "AlbumId", input.getAlbumId());
            extractInputParameterAndValue(columns, params, "MediaTypeId", input.getMediaTypeId());
            extractInputParameterAndValue(columns, params, "GenreId", input.getGenreId());
            extractInputParameterAndValue(columns, params, "Composer", input.getComposer());
            extractInputParameterAndValue(columns, params, "Milliseconds", input.getComposer());
            extractInputParameterAndValue(columns, params, "Bytes", input.getBytes());
            extractInputParameterAndValue(columns, params, "UnitPrice", input.getUnitPrice());

            return createJdbcSpec(columns, params, input.getLimit());
        }
    }

}
