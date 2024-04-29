package com.graphqljava.tutorial;

import com.graphqljava.tutorial.model.chinook.Album;
import com.graphqljava.tutorial.model.chinook.Artist;
import com.graphqljava.tutorial.model.chinook.Customer;
import com.graphqljava.tutorial.model.chinook.Employee;
import com.graphqljava.tutorial.model.chinook.Genre;
import com.graphqljava.tutorial.model.chinook.Invoice;
import com.graphqljava.tutorial.model.chinook.InvoiceLine;
import com.graphqljava.tutorial.model.chinook.MediaType;
import com.graphqljava.tutorial.model.chinook.Playlist;
import com.graphqljava.tutorial.model.chinook.PlaylistTrack;
import com.graphqljava.tutorial.model.chinook.Track;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TutorialGraphQLTest {

    @LocalServerPort
    private int randomServerPort;

    private HttpGraphQlTester tester;

    @BeforeEach
    public void beforeEach() {
        WebTestClient webTestClient =
                WebTestClient.bindToServer()
                        .baseUrl("http://localhost:" + randomServerPort + "/graphql")
                        .build();
        tester = HttpGraphQlTester.create(webTestClient);
    }

    @Test
    public void albums_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Albums (
                input: {
                    limit: 1
                })
              {
                AlbumId
                ArtistId
                Title
                Tracks (
                  input: {
                    limit: 1
                  })
                {
                  TrackId
                  AlbumId
                  Bytes
                  Composer
                  GenreId
                  MediaTypeId
                  Milliseconds
                  Name
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Albums");
        path.hasValue();
        GraphQlTester.EntityList<Album> entityListAlbum = path.entityList(Album.class);
        entityListAlbum.hasSize(1);

        Album album = entityListAlbum.get().getFirst();
        Assertions.assertNotNull(album);
        Album expectedAlbum = new Album(
            1,
            "For Those About To Rock We Salute You",
            1);
        Assertions.assertEquals(expectedAlbum, album);

        path = response.path("Albums[0].Tracks");
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            1,
            1,
            1,
            "Angus Young, Malcolm Young, Brian Johnson",
            343_719,
            11_170_334,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);
    }

    @Test
    public void artists_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Artists (
                input: {
                  limit: 1
                })
              {
                ArtistId
                Name
                Albums (
                  input: {
                    limit: 1
                  })
              {
                  AlbumId
                  ArtistId
                  Title
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Artists");
        path.hasValue();
        GraphQlTester.EntityList<Artist> entityListAlbum = path.entityList(Artist.class);
        entityListAlbum.hasSize(1);

        Artist artist = entityListAlbum.get().getFirst();
        Assertions.assertNotNull(artist);
        Artist expectedArtist = new Artist(1,"AC/DC");
        Assertions.assertEquals(expectedArtist, artist);

        path = response.path("Artists[0].Albums");
        GraphQlTester.EntityList<Album> entityListTrack = path.entityList(Album.class);
        entityListTrack.hasSize(1);

        Album album = entityListTrack.get().getFirst();
        Assertions.assertNotNull(album);
        Album expectedAlbum = new Album(1, "For Those About To Rock We Salute You", 1);
        Assertions.assertEquals(expectedAlbum, album);
    }

    @Test
    public void customers_Limit1_SupportRep() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Customers (
                input: {
                  limit: 1
                })
              {
                Address
                City
                Company
                Country
                CustomerId
                Email
                Fax
                FirstName
                LastName
                Phone
                PostalCode
                State
                SupportRepId
                SupportRep {
                  Address
                  BirthDate
                  City
                  Country
                  Email
                  EmployeeId
                  Fax
                  FirstName
                  HireDate
                  LastName
                  Phone
                  PostalCode
                  ReportsTo
                  State
                  Title
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Customers");
        path.hasValue();
        GraphQlTester.EntityList<Customer> entityListAlbum = path.entityList(Customer.class);
        entityListAlbum.hasSize(1);

        Customer customer = entityListAlbum.get().getFirst();
        Assertions.assertNotNull(customer);
        Customer expectedCustomer = new Customer(
            1,
            "Luís",
            "Gonçalves",
            "Embraer - Empresa Brasileira de Aeronáutica S.A.",
            "Av. Brigadeiro Faria Lima, 2170",
            "São José dos Campos",
            "SP",
            "Brazil",
            "12227-000",
            "+55 (12) 3923-5555",
            "+55 (12) 3923-5566",
            "luisg@embraer.com.br",
            3
        );
        Assertions.assertEquals(expectedCustomer, customer);

        path = response.path("Customers[0].SupportRep");
        path.hasValue();
        GraphQlTester.Entity<Employee, ?> entityEmployee = path.entity(Employee.class);

        Employee employee = entityEmployee.get();
        Assertions.assertNotNull(employee);
        Employee expectedEmployee = new Employee(
            3,
            "Peacock",
            "Jane",
            "Sales Support Agent",
            2,
            "1973-08-29 00:00:00",
            "2002-04-01 00:00:00",
            "1111 6 Ave SW",
            "Calgary",
            "AB",
            "Canada",
            "T2P 5M5",
            "+1 (403) 262-3443",
            "+1 (403) 262-6712",
            "jane@chinookcorp.com"
        );
        Assertions.assertEquals(expectedEmployee, employee);
    }

    @Test
    public void customers_Limit1_Invoices_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Customers (
                input: {
                  limit: 1
                })
              {
                Address
                City
                Company
                Country
                CustomerId
                Email
                Fax
                FirstName
                LastName
                Phone
                PostalCode
                State
                SupportRepId
                Invoices (
                  input: {
                    limit: 1
                  })
                {
                  BillingAddress
                  BillingCity
                  BillingCountry
                  BillingPostalCode
                  BillingState
                  CustomerId
                  InvoiceDate
                  InvoiceId
                  Total
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Customers");
        path.hasValue();
        GraphQlTester.EntityList<Customer> entityListAlbum = path.entityList(Customer.class);
        entityListAlbum.hasSize(1);

        Customer customer = entityListAlbum.get().getFirst();
        Assertions.assertNotNull(customer);
        Customer expectedCustomer = new Customer(
            1,
            "Luís",
            "Gonçalves",
            "Embraer - Empresa Brasileira de Aeronáutica S.A.",
            "Av. Brigadeiro Faria Lima, 2170",
            "São José dos Campos",
            "SP",
            "Brazil",
            "12227-000",
            "+55 (12) 3923-5555",
            "+55 (12) 3923-5566",
            "luisg@embraer.com.br",
            3
        );
        Assertions.assertEquals(expectedCustomer, customer);

        path = response.path("Customers[0].Invoices");
        path.hasValue();
        GraphQlTester.EntityList<Invoice> entityListInvoice = path.entityList(Invoice.class);
        entityListInvoice.hasSize(1);

        Invoice invoice = entityListInvoice.get().getFirst();
        Assertions.assertNotNull(invoice);
        Invoice expectedInvoice = new Invoice(
            98,
            1,
            "2010-03-11 00:00:00",
            "Av. Brigadeiro Faria Lima, 2170",
            "São José dos Campos",
            "SP",
            "Brazil",
            "12227-000",
            3.98f
        );
        Assertions.assertEquals(expectedInvoice, invoice);
    }

    @Test
    public void employees_Limit1_Manager() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Employees (
                input: {
                  limit: 1
                })
              {
                Address
                BirthDate
                City
                Country
                Email
                EmployeeId
                Fax
                FirstName
                HireDate
                LastName
                Phone
                PostalCode
                ReportsTo
                State
                Manager {
                  Address
                  BirthDate
                  City
                  Country
                  Email
                  EmployeeId
                  Fax
                  FirstName
                  HireDate
                  LastName
                  Phone
                  PostalCode
                  ReportsTo
                  State
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Employees");
        path.hasValue();
        GraphQlTester.EntityList<Employee> entityListEmployee = path.entityList(Employee.class);
        entityListEmployee.hasSize(1);

        Employee employee = entityListEmployee.get().getFirst();
        Assertions.assertNotNull(employee);
        Employee expectedEmployee = new Employee(
            1,
            "Adams",
            "Andrew",
            null, //"General Manager",
            0,
            "1962-02-18 00:00:00",
            "2002-08-14 00:00:00",
            "11120 Jasper Ave NW",
            "Edmonton",
            "AB",
            "Canada",
            "T5K 2N1",
            "+1 (780) 428-9482",
            "+1 (780) 428-3457",
            "andrew@chinookcorp.com"
        );
        Assertions.assertEquals(expectedEmployee, employee);

        path = response.path("Employees[0].Manager");
        path.valueIsNull();
    }

    @Test
    public void employees_Limit1_Customers_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Employees (
                input: {
                  limit: 1
                })
              {
                Address
                BirthDate
                City
                Country
                Email
                EmployeeId
                Fax
                FirstName
                HireDate
                LastName
                Phone
                PostalCode
                ReportsTo
                State
                Customers (
                  input: {
                    limit: 1
                  })
                {
                  Address
                  City
                  Company
                  Country
                  CustomerId
                  Email
                  Fax
                  FirstName
                  LastName
                  Phone
                  PostalCode
                  State
                  SupportRepId
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Employees");
        path.hasValue();
        GraphQlTester.EntityList<Employee> entityListEmployee = path.entityList(Employee.class);
        entityListEmployee.hasSize(1);

        Employee employee = entityListEmployee.get().getFirst();
        Assertions.assertNotNull(employee);
        Employee expectedEmployee = new Employee(
            1,
            "Adams",
            "Andrew",
            null, //"General Manager",
            0,
            "1962-02-18 00:00:00",
            "2002-08-14 00:00:00",
            "11120 Jasper Ave NW",
            "Edmonton",
            "AB",
            "Canada",
            "T5K 2N1",
            "+1 (780) 428-9482",
            "+1 (780) 428-3457",
            "andrew@chinookcorp.com"
        );
        Assertions.assertEquals(expectedEmployee, employee);

        path = response.path("Employees[0].Customers");
        path.hasValue();
        GraphQlTester.EntityList<Customer> entityListCustomer = path.entityList(Customer.class);
        entityListEmployee.hasSize(1);
        Customer customer = entityListCustomer.get().getFirst();
        Assertions.assertNotNull(customer);
        Customer expectedCustomer = new Customer(
            1,
            "Luís",
            "Gonçalves",
            "Embraer - Empresa Brasileira de Aeronáutica S.A.",
            "Av. Brigadeiro Faria Lima, 2170",
            "São José dos Campos",
            "SP",
            "Brazil",
            "12227-000",
            "+55 (12) 3923-5555",
            "+55 (12) 3923-5566",
            "luisg@embraer.com.br",
            3
        );
        Assertions.assertEquals(expectedCustomer, customer);
    }

    @Test
    public void employees_Limit1_Reports() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Employees (
                input: {
                  limit: 1
                })
              {
                Address
                BirthDate
                City
                Country
                Email
                EmployeeId
                Fax
                FirstName
                HireDate
                LastName
                Phone
                PostalCode
                ReportsTo
                State
                Reports {
                  Address
                  BirthDate
                  City
                  Country
                  Email
                  EmployeeId
                  Fax
                  FirstName
                  HireDate
                  LastName
                  Phone
                  PostalCode
                  ReportsTo
                  State
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Employees");
        path.hasValue();
        GraphQlTester.EntityList<Employee> entityListEmployee = path.entityList(Employee.class);
        entityListEmployee.hasSize(1);

        Employee employee = entityListEmployee.get().getFirst();
        Assertions.assertNotNull(employee);
        Employee expectedEmployee = new Employee(
            1,
            "Adams",
            "Andrew",
            null, //"General Manager",
            0,
            "1962-02-18 00:00:00",
            "2002-08-14 00:00:00",
            "11120 Jasper Ave NW",
            "Edmonton",
            "AB",
            "Canada",
            "T5K 2N1",
            "+1 (780) 428-9482",
            "+1 (780) 428-3457",
            "andrew@chinookcorp.com"
        );
        Assertions.assertEquals(expectedEmployee, employee);

        path = response.path("Employees[0].Reports");
        path.valueIsNull();
    }

    @Test
    public void genres_Limit1_Tracks_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
               Genres (
                 input: {
                   limit: 1
                 })
               {
                 GenreId
                 Name
                 Tracks (
                   input: {
                     limit: 1
                   })
                 {
                   AlbumId
                   Bytes
                   Composer
                   GenreId
                   MediaTypeId
                   Name
                   Milliseconds
                   TrackId
                   UnitPrice
                 }
               }
             }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Genres");
        path.hasValue();
        GraphQlTester.EntityList<Genre> entityListEmployee = path.entityList(Genre.class);
        entityListEmployee.hasSize(1);

        Genre genre = entityListEmployee.get().getFirst();
        Assertions.assertNotNull(genre);
        Genre expectedGenre = new Genre(1, "Rock");
        Assertions.assertEquals(expectedGenre, genre);

        path = response.path("Genres[0].Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            1,
            1,
            1,
            "Angus Young, Malcolm Young, Brian Johnson",
            343_719,
            11_170_334,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);
    }

    @Test
    public void invoices_Limit1_Customer() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Invoices (
                input: {
                  limit: 1
                })
              {
                BillingAddress
                BillingCity
                BillingCountry
                BillingPostalCode
                BillingState
                CustomerId
                InvoiceDate
                InvoiceId
                Total
                Customer {
                  Address
                  City
                  Country
                  CustomerId
                  Email
                  Fax
                  FirstName
                  LastName
                  Phone
                  PostalCode
                  State
                  SupportRepId
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Invoices");
        path.hasValue();
        GraphQlTester.EntityList<Invoice> entityListInvoice = path.entityList(Invoice.class);
        entityListInvoice.hasSize(1);

        Invoice invoice = entityListInvoice.get().getFirst();
        Assertions.assertNotNull(invoice);
        Invoice expectedInvoice = new Invoice(
            1,
            2,
            "2009-01-01 00:00:00",
            "Theodor-Heuss-Straße 34",
            "Stuttgart",
            null,
            "Germany",
            "70174",
            1.98f
        );
        Assertions.assertEquals(expectedInvoice, invoice);

        path = response.path("Invoices[0].Customer");
        path.hasValue();
        GraphQlTester.Entity<Customer, ?> entityCustomer = path.entity(Customer.class);

        Customer customer = entityCustomer.get();
        Assertions.assertNotNull(customer);
        Customer expectedCustomer = new Customer(
            2,
            "Leonie",
            "Köhler",
            null,
            "Theodor-Heuss-Straße 34",
            "Stuttgart",
            null,
            "Germany",
            "70174",
            "+49 0711 2842222",
            null,
            "leonekohler@surfeu.de",
            5
        );
        Assertions.assertEquals(expectedCustomer, customer);
    }

    @Test
    public void invoices_Limit1_InvoiceLines_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Invoices (
                input: {
                  limit: 1
                })
              {
                BillingAddress
                BillingCity
                BillingCountry
                BillingPostalCode
                BillingState
                CustomerId
                InvoiceDate
                InvoiceId
                Total
                InvoiceLines (
                  input: {
                    limit: 1
                  })
                {
                  InvoiceId
                  InvoiceLineId
                  Quantity
                  TrackId
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Invoices");
        path.hasValue();
        GraphQlTester.EntityList<Invoice> entityListInvoice = path.entityList(Invoice.class);
        entityListInvoice.hasSize(1);

        Invoice invoice = entityListInvoice.get().getFirst();
        Assertions.assertNotNull(invoice);
        Invoice expectedInvoice = new Invoice(
                1,
                2,
                "2009-01-01 00:00:00",
                "Theodor-Heuss-Straße 34",
                "Stuttgart",
                null,
                "Germany",
                "70174",
                1.98f
        );
        Assertions.assertEquals(expectedInvoice, invoice);

        path = response.path("Invoices[0].InvoiceLines");
        path.hasValue();
        GraphQlTester.EntityList<InvoiceLine> entityListInvoiceLine = path.entityList(InvoiceLine.class);

        InvoiceLine invoiceLine = entityListInvoiceLine.get().getFirst();
        Assertions.assertNotNull(invoiceLine);
        InvoiceLine expectedInvoiceLine = new InvoiceLine(
            1,
            1,
            2,
            0.99f,
            1
        );
        Assertions.assertEquals(expectedInvoiceLine, invoiceLine);
    }

    @Test
    public void invoiceLines_Limit1_Invoice() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              InvoiceLines (
                input: {
                  limit: 1
                })
              {
                InvoiceId
                InvoiceLineId
                Quantity
                TrackId
                UnitPrice
                Invoice {
                  BillingAddress
                  BillingCity
                  BillingCountry
                  BillingPostalCode
                  BillingState
                  CustomerId
                  InvoiceDate
                  InvoiceId
                  Total
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("InvoiceLines");
        path.hasValue();
        GraphQlTester.EntityList<InvoiceLine> entityListInvoiceLine = path.entityList(InvoiceLine.class);
        entityListInvoiceLine.hasSize(1);

        InvoiceLine invoiceLine = entityListInvoiceLine.get().getFirst();
        Assertions.assertNotNull(invoiceLine);
        InvoiceLine expectedInvoiceLine = new InvoiceLine(
            1,
            1,
            2,
            0.99f,
            1
        );
        Assertions.assertEquals(expectedInvoiceLine, invoiceLine);

        path = response.path("InvoiceLines[0].Invoice");
        path.hasValue();
        GraphQlTester.Entity<Invoice, ?> entityInvoice = path.entity(Invoice.class);

        Invoice invoice = entityInvoice.get();
        Assertions.assertNotNull(invoice);
        Invoice expectedInvoice = new Invoice(
            1,
            2,
            "2009-01-01 00:00:00",
            "Theodor-Heuss-Straße 34",
            "Stuttgart",
            null,
            "Germany",
            "70174",
            1.98f
        );
        Assertions.assertEquals(expectedInvoice, invoice);
    }

    @Test
    public void invoiceLines_Limit1_Track() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              InvoiceLines (
                input: {
                  limit: 1
                })
              {
                InvoiceId
                InvoiceLineId
                Quantity
                TrackId
                UnitPrice
                Track {
                  AlbumId
                  Bytes
                  Composer
                  GenreId
                  MediaTypeId
                  Milliseconds
                  Name
                  TrackId
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("InvoiceLines");
        path.hasValue();
        GraphQlTester.EntityList<InvoiceLine> entityListInvoiceLine = path.entityList(InvoiceLine.class);
        entityListInvoiceLine.hasSize(1);

        InvoiceLine invoiceLine = entityListInvoiceLine.get().getFirst();
        Assertions.assertNotNull(invoiceLine);
        InvoiceLine expectedInvoiceLine = new InvoiceLine(
            1,
            1,
            2,
            0.99f,
            1
        );
        Assertions.assertEquals(expectedInvoiceLine, invoiceLine);

        path = response.path("InvoiceLines[0].Track");
        path.hasValue();
        GraphQlTester.Entity<Track, ?> entityTrack = path.entity(Track.class);

        Track track = entityTrack.get();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            2,
            "Balls to the Wall",
            2,
            1,
            2,
            null,
            342_562,
            5_510_424,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);
    }

    @Test
    public void mediaTypes_Limit1_Tracks_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              MediaTypes (
                input: {
                  limit: 1
                })
              {
                MediaTypeId
                Name
                Tracks (
                  input: {
                    limit: 1
                  })
                {
                  AlbumId
                  Bytes
                  Composer
                  GenreId
                  MediaTypeId
                  Milliseconds
                  Name
                  TrackId
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("MediaTypes");
        path.hasValue();
        GraphQlTester.EntityList<MediaType> entityListMediaType = path.entityList(MediaType.class);
        entityListMediaType.hasSize(1);

        MediaType mediaType = entityListMediaType.get().getFirst();
        Assertions.assertNotNull(mediaType);
        MediaType expectedMediaType = new MediaType(1,"MPEG audio file");
        Assertions.assertEquals(expectedMediaType, mediaType);

        path = response.path("MediaTypes[0].Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityTrack = path.entityList(Track.class);

        Track track = entityTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
                1,
                "For Those About To Rock (We Salute You)",
                1,
                1,
                1,
                "Angus Young, Malcolm Young, Brian Johnson",
                343_719,
                11_170_334,
                0.99f
        );
        Assertions.assertEquals(expectedTrack, track);
    }

    @Test
    public void playlists_Limit1_PlaylistTracks_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Playlists (
                input: {
                  limit: 1
                })
              {
                PlaylistId
                PlaylistTracks (
                  input: {
                    limit: 1
                  })
                {
                  PlaylistId
                  TrackId
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Playlists");
        path.hasValue();
        GraphQlTester.EntityList<Playlist> entityListPlaylist = path.entityList(Playlist.class);
        entityListPlaylist.hasSize(1);

        Playlist playlist = entityListPlaylist.get().getFirst();
        Assertions.assertNotNull(playlist);
        Playlist expectedPlaylist = new Playlist(1,null);
        Assertions.assertEquals(expectedPlaylist, playlist);

        path = response.path("Playlists[0].PlaylistTracks");
        path.hasValue();
        GraphQlTester.EntityList<PlaylistTrack> entityTrack = path.entityList(PlaylistTrack.class);

        PlaylistTrack playlistTrack = entityTrack.get().getFirst();
        Assertions.assertNotNull(playlistTrack);
        PlaylistTrack expectedPlaylistTrack = new PlaylistTrack(1,3_402);
        Assertions.assertEquals(expectedPlaylistTrack, playlistTrack);
    }

    @Test
    public void playlistTracks_Limit1_Playlist() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              PlaylistTracks (
                input: {
                  limit: 1
                })
              {
                PlaylistId
                TrackId
                Playlist {
                  PlaylistId
                  Name
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("PlaylistTracks");
        path.hasValue();
        GraphQlTester.EntityList<PlaylistTrack> entityListPlaylistTrack = path.entityList(PlaylistTrack.class);
        entityListPlaylistTrack.hasSize(1);

        PlaylistTrack playlistTrack = entityListPlaylistTrack.get().getFirst();
        Assertions.assertNotNull(playlistTrack);
        PlaylistTrack expectedPlaylistTrack = new PlaylistTrack(1,3_402);
        Assertions.assertEquals(expectedPlaylistTrack, playlistTrack);

        path = response.path("PlaylistTracks[0].Playlist");
        path.hasValue();
        GraphQlTester.Entity<Playlist, ?> entityPlaylist = path.entity(Playlist.class);

        Playlist playlist = entityPlaylist.get();
        Assertions.assertNotNull(playlist);
        Assertions.assertNotNull(playlist);
        Playlist expectedPlaylist = new Playlist(1,"Music");
        Assertions.assertEquals(expectedPlaylist, playlist);
    }

    @Test
    public void playlistTracks_Limit1_Track() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              PlaylistTracks (
                input: {
                  limit: 1
                })
              {
                PlaylistId
                TrackId
                Track {
                  AlbumId
                  Bytes
                  Composer
                  GenreId
                  MediaTypeId
                  Milliseconds
                  Name
                  TrackId
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("PlaylistTracks");
        path.hasValue();
        GraphQlTester.EntityList<PlaylistTrack> entityListPlaylistTrack = path.entityList(PlaylistTrack.class);
        entityListPlaylistTrack.hasSize(1);

        PlaylistTrack playlistTrack = entityListPlaylistTrack.get().getFirst();
        Assertions.assertNotNull(playlistTrack);
        PlaylistTrack expectedPlaylistTrack = new PlaylistTrack(1,3_402);
        Assertions.assertEquals(expectedPlaylistTrack, playlistTrack);

        path = response.path("PlaylistTracks[0].Track");
        path.hasValue();
        GraphQlTester.Entity<Track, ?> entityTrack = path.entity(Track.class);

        Track track = entityTrack.get();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            3_402,
            "Band Members Discuss Tracks from \"Revelations\"",
            271,
            23,
            3,
            null,
            294_294,
            61_118_891,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);
    }

    @Test
    public void tracks_Limit1_Album() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Tracks (
                input: {
                  limit: 1
                })
              {
                AlbumId
                Bytes
                Composer
                GenreId
                MediaTypeId
                Milliseconds
                Name
                TrackId
                UnitPrice
                Album {
                  AlbumId
                  ArtistId
                  Title
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            1,
            1,
            1,
            "Angus Young, Malcolm Young, Brian Johnson",
            343_719,
            11_170_334,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].Album");
        path.hasValue();
        GraphQlTester.Entity<Album, ?> entityAlbum = path.entity(Album.class);

        Album album = entityAlbum.get();
        Assertions.assertNotNull(album);
        Album expectedAlbum = new Album(1,"For Those About To Rock We Salute You", 1);
        Assertions.assertEquals(expectedAlbum, album);
    }

    @Test
    public void tracks_Limit1_Genre() {
        GraphQlTester.Request<?> request = tester.document(
                """
                {
                  Tracks (
                    input: {
                      limit: 1
                    })
                  {
                    AlbumId
                    Bytes
                    Composer
                    GenreId
                    MediaTypeId
                    Milliseconds
                    Name
                    TrackId
                    UnitPrice
                    Genre {
                      GenreId
                      Name
                    }
                  }
                }
                """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
                1,
                "For Those About To Rock (We Salute You)",
                1,
                1,
                1,
                "Angus Young, Malcolm Young, Brian Johnson",
                343_719,
                11_170_334,
                0.99f
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].Genre");
        path.hasValue();
        GraphQlTester.Entity<Genre, ?> entityAlbum = path.entity(Genre.class);

        Genre genre = entityAlbum.get();
        Assertions.assertNotNull(genre);
        Assertions.assertNotNull(genre);
        Genre expectedGenre = new Genre(1,"Rock");
        Assertions.assertEquals(expectedGenre, genre);
    }

    @Test
    public void tracks_Limit1_InvoiceLines_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Tracks (
                input: {
                  limit: 1
                })
              {
                AlbumId
                Bytes
                Composer
                GenreId
                MediaTypeId
                Milliseconds
                Name
                TrackId
                UnitPrice
                InvoiceLines (
                  input: {
                    limit: 1
                  })
                {
                  InvoiceId
                  Quantity
                  InvoiceLineId
                  TrackId
                  UnitPrice
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
                1,
                "For Those About To Rock (We Salute You)",
                1,
                1,
                1,
                "Angus Young, Malcolm Young, Brian Johnson",
                343_719,
                11_170_334,
                0.99f
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].InvoiceLines");
        path.hasValue();
        GraphQlTester.EntityList<InvoiceLine> entityListInvoiceLine = path.entityList(InvoiceLine.class);
        entityListInvoiceLine.hasSize(1);

        InvoiceLine invoiceLine = entityListInvoiceLine.get().getFirst();
        Assertions.assertNotNull(invoiceLine);
        InvoiceLine expectedInvoiceLine = new InvoiceLine(579, 108, 1, 0.99f,1);
        Assertions.assertEquals(expectedInvoiceLine, invoiceLine);
    }

    @Test
    public void tracks_Limit1_PlaylistTracks_Limit1() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Tracks(
                input: {
                  limit: 1
                })
              {
                AlbumId
                Bytes
                Composer
                GenreId
                MediaTypeId
                Milliseconds
                Name
                TrackId
                UnitPrice
                PlaylistTracks (
                  input: {
                    limit: 1
                  })
                {
                  PlaylistId
                  TrackId
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            1,
            1,
            1,
            "Angus Young, Malcolm Young, Brian Johnson",
            343_719,
            11_170_334,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].PlaylistTracks");
        path.hasValue();
        GraphQlTester.EntityList<PlaylistTrack> entityListPlaylistTrack = path.entityList(PlaylistTrack.class);
        entityListPlaylistTrack.hasSize(1);

        PlaylistTrack playlistTrack = entityListPlaylistTrack.get().getFirst();
        Assertions.assertNotNull(playlistTrack);
        PlaylistTrack expectedPlaylistTrack = new PlaylistTrack(1, 1);
        Assertions.assertEquals(expectedPlaylistTrack, playlistTrack);
    }

    @Test
    public void tracks_Limit1_MediaType() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Tracks(
                input: {
                  limit: 1
                })
              {
                AlbumId
                Bytes
                Composer
                GenreId
                MediaTypeId
                Milliseconds
                Name
                TrackId
                UnitPrice
                MediaType {
                  Name
                  MediaTypeId
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(1);

        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            1,
            1,
            1,
            "Angus Young, Malcolm Young, Brian Johnson",
            343_719,
            11_170_334,
            0.99f
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].MediaType");
        path.hasValue();
        GraphQlTester.Entity<MediaType, ?> entityMediaTye = path.entity(MediaType.class);

        MediaType mediaType = entityMediaTye.get();
        Assertions.assertNotNull(mediaType);
        MediaType expectedMediaType = new MediaType(1, "MPEG audio file");
        Assertions.assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void tracks_Limit1_Album_Tracks_Limit3_Name() {
        GraphQlTester.Request<?> request = tester.document(
            """
            {
              Tracks (
                input: {
                    limit: 3
                })
              {
                TrackId
                Name
                Album (
                  input: {
                    limit: 3
                    })
                {
                  AlbumId
                  Tracks (
                    input: {
                        limit: 3,
                        Name: "Balls to the Wall"
                    })
                  {
                    Name
                  }
                }
              }
            }
            """
        );

        GraphQlTester.Response response = request.execute();

        GraphQlTester.Path path = response.path("Tracks");
        path.hasValue();
        GraphQlTester.EntityList<Track> entityListTrack = path.entityList(Track.class);
        entityListTrack.hasSize(3);

        // Track 0
        Track track = entityListTrack.get().getFirst();
        Assertions.assertNotNull(track);
        Track expectedTrack = new Track(
            1,
            "For Those About To Rock (We Salute You)",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[0].Album");
        path.hasValue();
        GraphQlTester.Entity<Album, ?> entityAlbum = path.entity(Album.class);

        Album album = entityAlbum.get();
        Assertions.assertNotNull(album);
        Album expectedAlbum = new Album(1, null, null);
        Assertions.assertEquals(expectedAlbum, album);

        // Track 1
        track = entityListTrack.get().get(1);
        Assertions.assertNotNull(track);
        expectedTrack = new Track(
            2,
            "Balls to the Wall",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[1].Album");
        path.hasValue();
        entityAlbum = path.entity(Album.class);

        album = entityAlbum.get();
        Assertions.assertNotNull(album);
        expectedAlbum = new Album(2, null, null);
        Assertions.assertEquals(expectedAlbum, album);

        path = response.path("Tracks[1].Album.Tracks");
        path.hasValue();

        GraphQlTester.EntityList<Track> entityListTrack2 = path.entityList(Track.class);
        entityListTrack2.hasSize(1);

        entityListTrack2 = path.entityList(Track.class);
        track = entityListTrack2.get().getFirst();
        Assertions.assertNotNull(track);
        expectedTrack = new Track(
            null,
            "Balls to the Wall",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        Assertions.assertEquals(expectedTrack, track);

        // Track 2
        track = entityListTrack.get().get(2);
        Assertions.assertNotNull(track);
        expectedTrack = new Track(
            3,
            "Fast As a Shark",
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        Assertions.assertEquals(expectedTrack, track);

        path = response.path("Tracks[2].Album");
        path.hasValue();
        entityAlbum = path.entity(Album.class);

        album = entityAlbum.get();
        Assertions.assertNotNull(album);
        expectedAlbum = new Album(3, null, null);
        Assertions.assertEquals(expectedAlbum, album);
    }
}
