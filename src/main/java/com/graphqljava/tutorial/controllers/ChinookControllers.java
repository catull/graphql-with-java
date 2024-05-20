package com.graphqljava.tutorial.controllers;

import com.graphqljava.tutorial.jpa.chinook.AlbumRepository;
import com.graphqljava.tutorial.jpa.chinook.ArtistRepository;
import com.graphqljava.tutorial.jpa.chinook.CustomerRepository;
import com.graphqljava.tutorial.jpa.chinook.EmployeeRepository;
import com.graphqljava.tutorial.jpa.chinook.GenreRepository;
import com.graphqljava.tutorial.jpa.chinook.InvoiceLineRepository;
import com.graphqljava.tutorial.jpa.chinook.InvoiceRepository;
import com.graphqljava.tutorial.jpa.chinook.MediaTypeRepository;
import com.graphqljava.tutorial.jpa.chinook.PlaylistRepository;
import com.graphqljava.tutorial.jpa.chinook.PlaylistTrackRepository;
import com.graphqljava.tutorial.jpa.chinook.TrackRepository;
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

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

public class ChinookControllers {
    @Controller
    public static class AlbumController {
        private final AlbumRepository albumRepository;

        public AlbumController(final AlbumRepository albumRepository) {
            this.albumRepository = albumRepository;
        }

        @QueryMapping
        public List<ChinookModels.Album> Albums(@Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.albumRepository.findAll(Example.of(new ChinookModels.Album(
                    input.getAlbumId(),
                    input.getTitle(),
                    input.getArtistId()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public List<ChinookModels.Album> Albums(final ChinookModels.Artist artist, @Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }

            if (null == input.getAlbumId()) {
                input.setAlbumId(artist.getArtistId());
            }

            return Albums(input);
        }

        @SchemaMapping
        public ChinookModels.Album Album(final ChinookModels.Track track, @Argument AlbumInput input) {
            if (null == input) {
                input = new AlbumInput();
            }
            if (null == input.getAlbumId()) {
                input.setAlbumId(track.getAlbumId());
            }

            return this.albumRepository.findOne(Example.of(new ChinookModels.Album(
                    input.getAlbumId(),
                    input.getTitle(),
                    input.getArtistId()
            ))).orElse(null);
        }
    }

    @Controller
    public static class ArtistController {
        private final ArtistRepository artistRepository;

        public ArtistController(final ArtistRepository artistRepository) {
            this.artistRepository = artistRepository;
        }

        @QueryMapping
        public List<ChinookModels.Artist> Artists(@Argument ArtistInput input) {
            if (null == input) {
                input = new ArtistInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.artistRepository.findAll(Example.of(new ChinookModels.Artist(
                    input.getArtistId(),
                    input.getName()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.Artist Artist(final ChinookModels.Album album, @Argument ArtistInput input) {
            if (null == input) {
                input = new ArtistInput();
            }
            if (null == input.getArtistId()) {
                input.setArtistId(album.getArtistId());
            }

            return this.artistRepository.findOne(Example.of(new ChinookModels.Artist(
                    input.getArtistId(),
                    input.getName()
            ))).orElse(null);
        }
    }

    @Controller
    public static class CustomerController {
        private final CustomerRepository customerRepository;

        public CustomerController(final CustomerRepository customerRepository) {
            this.customerRepository = customerRepository;
        }

        @QueryMapping
        public List<ChinookModels.Customer> Customers(@Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.customerRepository.findAll(Example.of(new ChinookModels.Customer(
                    input.getCustomerId(),
                    input.getFirstName(),
                    input.getLastName(),
                    input.getCompany(),
                    input.getAddress(),
                    input.getCity(),
                    input.getState(),
                    input.getCountry(),
                    input.getPostalCode(),
                    input.getPhone(),
                    input.getFax(),
                    input.getEmail(),
                    input.getSupportRepId()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.Customer Customer(final ChinookModels.Invoice invoice, @Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }
            if (null == input.getCustomerId()) {
                input.setCustomerId(invoice.getCustomerId());
            }

            return this.customerRepository.findOne(Example.of(new ChinookModels.Customer(
                    input.getCustomerId(),
                    input.getFirstName(),
                    input.getLastName(),
                    input.getCompany(),
                    input.getAddress(),
                    input.getCity(),
                    input.getState(),
                    input.getCountry(),
                    input.getPostalCode(),
                    input.getPhone(),
                    input.getFax(),
                    input.getEmail(),
                    input.getSupportRepId()
            ))).orElse(null);
        }

        @SchemaMapping
        public List<ChinookModels.Customer> Customers(final ChinookModels.Employee employee, @Argument CustomerInput input) {
            if (null == input) {
                input = new CustomerInput();
            }
            if (null == input.getCustomerId()) {
                input.setCustomerId(employee.getEmployeeId());
            }

            return Customers(input);
        }
    }

    @Controller
    public static class EmployeeController {
        private final EmployeeRepository employeeRepository;

        public EmployeeController(final EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        public ChinookModels.Employee Employee(@Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }

            return this.employeeRepository.findOne(Example.of(new ChinookModels.Employee(
                    input.getEmployeeId(),
                    input.getFirstName(),
                    input.getLastName(),
                    input.getTitle(),
                    input.getReportsTo(),
                    input.getBirthDate(),
                    input.getHireDate(),
                    input.getAddress(),
                    input.getCity(),
                    input.getState(),
                    input.getCountry(),
                    input.getPostalCode(),
                    input.getPhone(),
                    input.getFax(),
                    input.getEmail()
            ))).orElse(null);
        }

        @QueryMapping
        public List<ChinookModels.Employee> Employees(@Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.employeeRepository.findAll(Example.of(new ChinookModels.Employee(
                    input.getEmployeeId(),
                    input.getFirstName(),
                    input.getLastName(),
                    input.getTitle(),
                    input.getReportsTo(),
                    input.getBirthDate(),
                    input.getHireDate(),
                    input.getAddress(),
                    input.getCity(),
                    input.getState(),
                    input.getCountry(),
                    input.getPostalCode(),
                    input.getPhone(),
                    input.getFax(),
                    input.getEmail()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.Employee SupportRep(final ChinookModels.Customer customer, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getEmployeeId()) {
                input.setEmployeeId(customer.getSupportRepId());
            }

            return Employee(input);
        }

        @SchemaMapping
        public ChinookModels.Employee Manager(final ChinookModels.Employee employee, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getEmployeeId()) {
                input.setEmployeeId(employee.getReportsTo());
            }

            return Employee(input);
        }

        @SchemaMapping
        public ChinookModels.Employee Reports(final ChinookModels.Employee employee, @Argument EmployeeInput input) {
            if (null == input) {
                input = new EmployeeInput();
            }
            if (null == input.getReportsTo()) {
                input.setReportsTo(employee.getReportsTo());
            }

            return Employee(input);
        }
    }

    @Controller
    public static class GenreController {
        private final GenreRepository genreRepository;

        public GenreController(final GenreRepository genreRepository) {
            this.genreRepository = genreRepository;
        }

        @QueryMapping
        public List<ChinookModels.Genre> Genres(@Argument GenreInput input) {
            if (null == input) {
                input = new GenreInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.genreRepository.findAll(Example.of(new ChinookModels.Genre(
                    input.getGenreId(),
                    input.getName()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.Genre Genre(final ChinookModels.Track track, @Argument GenreInput input) {
            if (null == input) {
                input = new GenreInput();
            }
            if (null == input.getGenreId()) {
                input.setGenreId(track.getGenreId());
            }

            return this.genreRepository.findOne(Example.of(new ChinookModels.Genre(
                    input.getGenreId(),
                    input.getName()
            ))).orElse(null);
        }
    }

    @Controller
    public static class InvoiceController {
        private final InvoiceRepository invoiceRepository;

        public InvoiceController(final InvoiceRepository invoiceRepository) {
            this.invoiceRepository = invoiceRepository;
        }

        @QueryMapping
        public List<ChinookModels.Invoice> Invoices(@Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.invoiceRepository.findAll(Example.of(new ChinookModels.Invoice(
                    input.getInvoiceId(),
                    input.getCustomerId(),
                    input.getInvoiceDate(),
                    input.getBillingAddress(),
                    input.getBillingCity(),
                    input.getBillingState(),
                    input.getBillingCountry(),
                    input.getBillingPostalCode(),
                    input.getTotal()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public List<ChinookModels.Invoice> Invoices(final ChinookModels.Customer customer, @Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }

            if (null == input.getCustomerId()) {
                input.setCustomerId(customer.getCustomerId());
            }

            return Invoices(input);
        }

        @SchemaMapping
        public ChinookModels.Invoice Invoice(final ChinookModels.InvoiceLine invoiceLine, @Argument InvoiceInput input) {
            if (null == input) {
                input = new InvoiceInput();
            }
            if (null == input.getInvoiceId()) {
                input.setInvoiceId(invoiceLine.getInvoiceId());
            }

            return this.invoiceRepository.findOne(Example.of(new ChinookModels.Invoice(
                    input.getInvoiceId(),
                    input.getCustomerId(),
                    input.getInvoiceDate(),
                    input.getBillingAddress(),
                    input.getBillingCity(),
                    input.getBillingState(),
                    input.getBillingCountry(),
                    input.getBillingPostalCode(),
                    input.getTotal()
            ))).orElse(null);
        }
    }

    @Controller
    public static class InvoiceLineController {
        private final InvoiceLineRepository invoiceLineRepository;

        public InvoiceLineController(final InvoiceLineRepository invoiceLineRepository) {
            this.invoiceLineRepository = invoiceLineRepository;
        }

        @QueryMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(@Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.invoiceLineRepository.findAll(Example.of(new ChinookModels.InvoiceLine(
                    input.getInvoiceLineId(),
                    input.getInvoiceId(),
                    input.getTrackId(),
                    input.getUnitPrice(),
                    input.getQuantity()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(final ChinookModels.Invoice invoice, @Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }
            if (null == input.getInvoiceLineId()) {
                input.setInvoiceId(invoice.getInvoiceId());
            }

            return InvoiceLines(input);
        }

        @SchemaMapping
        public List<ChinookModels.InvoiceLine> InvoiceLines(final ChinookModels.Track track, @Argument InvoiceLineInput input) {
            if (null == input) {
                input = new InvoiceLineInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(track.getTrackId());
            }

            return InvoiceLines(input);
        }
    }

    @Controller
    public static class MediaTypeController {
        private final MediaTypeRepository mediaTypeRepository;

        public MediaTypeController(final MediaTypeRepository mediaTypeRepository) {
            this.mediaTypeRepository = mediaTypeRepository;
        }

        @QueryMapping
        public List<ChinookModels.MediaType> MediaTypes(@Argument MediaTypeInput input) {
            if (null == input) {
                input = new MediaTypeInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.mediaTypeRepository.findAll(Example.of(new ChinookModels.MediaType(
                    input.getMediaTypeId(),
                    input.getName()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.MediaType MediaType(final ChinookModels.Track track, @Argument MediaTypeInput input) {
            if (null == input) {
                input = new MediaTypeInput();
            }
            if (null == input.getMediaTypeId()) {
                input.setMediaTypeId(track.getMediaTypeId());
            }

            return this.mediaTypeRepository.findOne(Example.of(new ChinookModels.MediaType(
                    input.getMediaTypeId(),
                    input.getName()
            ))).orElse(null);
        }
    }

    @Controller
    public static class PlaylistController {
        private final PlaylistRepository playlistRepository;

        public PlaylistController(final PlaylistRepository playlistRepository) {
            this.playlistRepository = playlistRepository;
        }

        @QueryMapping
        public List<ChinookModels.Playlist> Playlists(@Argument PlaylistInput input) {
            if (null == input) {
                input = new PlaylistInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.playlistRepository.findAll(Example.of(new ChinookModels.Playlist(
                    input.getPlaylistId(),
                    input.getName()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public ChinookModels.Playlist Playlist(final ChinookModels.PlaylistTrack playlistTrack, @Argument PlaylistInput input) {
            if (null == input) {
                input = new PlaylistInput();
            }
            if (null == input.getPlaylistId()) {
                input.setPlaylistId(playlistTrack.getPlaylistId());
            }

            return this.playlistRepository.findOne(Example.of(new ChinookModels.Playlist(
                    input.getPlaylistId(),
                    input.getName()
            ))).orElse(null);
        }
    }

    @Controller
    public static class PlaylistTrackController {
        private final PlaylistTrackRepository playlistTrackRepository;

        public PlaylistTrackController(final PlaylistTrackRepository playlistTrackRepository) {
            this.playlistTrackRepository = playlistTrackRepository;
        }

        @QueryMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(@Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.playlistTrackRepository.findAll(Example.of(new ChinookModels.PlaylistTrack(
                    input.getPlaylistId(),
                    input.getTrackId()
            )), pageRequest).toList();
        }

        @SchemaMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(final ChinookModels.Playlist playlist, @Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }
            if (null == input.getPlaylistId()) {
                input.setPlaylistId(playlist.getPlaylistId());
            }

            return PlaylistTracks(input);
        }

        @SchemaMapping
        public List<ChinookModels.PlaylistTrack> PlaylistTracks(final ChinookModels.Track track, @Argument PlaylistTrackInput input) {
            if (null == input) {
                input = new PlaylistTrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(track.getTrackId());
            }

            return PlaylistTracks(input);
        }
    }

    @Controller
    public static class TrackController {
        private final TrackRepository trackRepository;

        public TrackController(final TrackRepository trackRepository) {
            this.trackRepository = trackRepository;
        }

        @QueryMapping
        public List<ChinookModels.Track> Tracks(@Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }

            int limit = input.getLimit() > 0 ? input.getLimit() : Integer.MAX_VALUE;
            PageRequest pageRequest = PageRequest.of(0, limit);

            return this.trackRepository.findAll(Example.of(new ChinookModels.Track(
                    input.getTrackId(),
                    input.getName(),
                    input.getAlbumId(),
                    input.getMediaTypeId(),
                    input.getGenreId(),
                    input.getComposer(),
                    input.getMilliseconds(),
                    input.getBytes(),
                    input.getUnitPrice()
            )), pageRequest).toList();
        }

        private ChinookModels.Track Track(@Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }

            return this.trackRepository.findOne(Example.of(new ChinookModels.Track(
                    input.getTrackId(),
                    input.getName(),
                    input.getAlbumId(),
                    input.getMediaTypeId(),
                    input.getGenreId(),
                    input.getComposer(),
                    input.getMilliseconds(),
                    input.getBytes(),
                    input.getUnitPrice()
            ))).orElse(null);
        }

        @SchemaMapping
        public ChinookModels.Track Track(final ChinookModels.InvoiceLine invoiceLine, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(invoiceLine.getTrackId());
            }

            return Track(input);
        }

        @SchemaMapping
        public ChinookModels.Track Track(final ChinookModels.PlaylistTrack playlistTrack, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getTrackId()) {
                input.setTrackId(playlistTrack.getTrackId());
            }

            return Track(input);
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.Album album, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getAlbumId()) {
                input.setAlbumId(album.getAlbumId());
            }

            return Tracks(input);
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.Genre genre, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getGenreId()) {
                input.setGenreId(genre.getGenreId());
            }

            return Tracks(input);
        }

        @SchemaMapping
        public List<ChinookModels.Track> Tracks(final ChinookModels.MediaType mediaType, @Argument TrackInput input) {
            if (null == input) {
                input = new TrackInput();
            }
            if (null == input.getMediaTypeId()) {
                input.setMediaTypeId(mediaType.getMediaTypeId());
            }

            return Tracks(input);
        }
    }
}
