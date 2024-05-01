- [What is GraphQL and why do people want it?](#orge7859e7)
- [How do I provide the GraphQL that people want, especially as a Java developer?](#org3940bb5)
- [How exactly do I build a GraphQL API server in Java for a real application?](#org9f0d658)
- [No, but really, how exactly do I build a GraphQL API server in Java for a real application?](#orgab05048)
  - [Step 6:  Choose [Docker Compose](https://docs.docker.com/compose/) for orchestrating a development database server.](#org01562ef)
  - [Step 7:  Choose the [Chinook](https://www.yugabyte.com/blog/postgresql-how-to-installing-the-chinook-sample-db-on-a-distributed-sql-database/) data model.](#orge37c2ff)
  - [Step 8:  Choose the [Spring Initializr](https://start.spring.io/) for bootstrapping the application.](#org901daeb)
  - [Step 9: [Create](https://netflix.github.io/dgs/#creating-a-schema) a GraphQL schema file.](#orgf41b657)
  - [Step 10:  Write Java model classes.](#org59aab0c)
  - [Step 11-14:  Write Java controller classes.  Annotate every controller.  Annotate every resolver/data-fetcher.  Implement those resolver/data-fetcher.](#org169a569)
  - [Step 15:  Upgrade *some* of those resolver/data-fetcher methods with the data loader pattern.](#org37a798d)
    - [Editorial Aside!](#orgac6576c)
- [Is this the *Only* way to build a GraphQL API server?](#org8d6ef13)
- [How to choose "Buy" over "Buy"](#org3f236d3)
- [About Me](#org3e1ab65)

> GraphQL is an innovative Application Performance Interface (API) that offers improvements in expressivity, efficiency, discoverability, and simplicity. This blog provides a comprehensive guide to implementing state-of-the-art GraphQL in Java for real-world applications. It covers the fundamental concepts of GraphQL, including its query language and data model, and highlights its similarities to programming languages and relational databases. The blog also offers a practical step-by-step recipe for building a GraphQL API server in Java, utilizing Spring Boot, Spring for GraphQL, and a relational database. It emphasizes the importance of persistence, flexibility, efficiency, and modernity in the design. Additionally, the blog discusses the trade-offs and challenges involved in the process. Finally, it presents an alternative path beyond the conventional approach, suggesting the potential benefits of a "GraphQL to SQL compiler" and exploring the option of acquiring a GraphQL API instead of building one. This guide is a valuable resource for Java developers seeking to create robust and efficient GraphQL API servers.


<a id="orge7859e7"></a>

# What is GraphQL and why do people want it?

GraphQL is an important evolution in the design of Application Performance Interfaces (API), but even today it can be difficult to know how to get started with GraphQL, how to move beyond "Getting Started" with GraphQL, and how to move beyond the conventional wisdom on GraphQL. This is especially true for Java. This guide attempts to cover all these bases in three steps. First, I'll tell you what GraphQL is, and as a bonus I'll tell you what GraphQL *really* is. Second, I'll show you how to implement state-of-the-art GraphQL in Java for a real application. Third, I'll offer you an alternative path beyond the state-of-the-art that may suit your needs better in every dimension. And, if you feel like skipping to [the end](#org3f236d3), who am I to stop you? It certainly will save a great deal of effort.

So, what *is* GraphQL? Well, GraphQL.org [says](https://graphql.org/learn/)

> GraphQL is a query language for your API, and a server-side runtime for executing queries using a type system you define for your data. GraphQL isn’t tied to any specific database or storage engine and is instead backed by your existing code and data.

That's not wrong, but let me take a few more runs at it from different directions. Sure, GraphQL *is* "a query language for your API", but you might as well just say that it *is* an API, or a way of building an API. That puts it in contrast with [REST](https://htmx.org/essays/rest-explained/), which GraphQL is an evolution from and an alternative to. GraphQL offers several improvements over REST (or at least, the [way](https://en.wikipedia.org/wiki/Richardson_Maturity_Model#Level_2:_HTTP_verbs) that REST is usually done):

-   **expressivity:** A client can say exactly what data they need from a server, no more and no less.
-   **efficiency:** Expressivity leads to efficiency gains, reducing network chatter and wasted bandwidth.
-   **discoverability:** In order to know what to say to a server, a client needs to know what *can* be said to a server. That's where discoverability comes in, allowing data consumers to know exactly what's available from data producers.
-   **simplicity:** GraphQL puts clients in the driver's seat, so there had better be some good ergonomics for driving. GraphQL's highly-regular machine-readable syntax, simple execution model, and simple [specification](https://spec.graphql.org/) lend themselves to inter-operable and composable tools:
    -   [query](https://altairgraphql.dev/) [tools](https://github.com/graphql/graphiql/tree/main/packages/graphiql#readme)
    -   [schema registries](https://the-guild.dev/graphql/hive)
    -   [gateways](https://the-guild.dev/graphql/mesh)
    -   [code generators](https://the-guild.dev/graphql/codegen)
    -   [client](https://commerce.nearform.com/open-source/urql/) [libraries](https://www.apollographql.com/docs/react/)

The evolution of GraphQL from REST is a fascinating history which we discuss at length in the first few sections of [The GraphQL Handbook](https://hasura.io/resources/graphql-handbook-2024).

But, what *else* is GraphQL. What *really* is GraphQL? GraphQL is *also* a data model for its query language and, despite the name, neither the query language nor the data model are very "graphy." The data model is [essentially](https://spec.graphql.org/October2021/#sec-JSON-Serialization) just JSON. The query language *looks* like JSON and can be boiled down to a few simple features:

-   **types:** A [type](https://spec.graphql.org/October2021/#sec-Types) is a simple value (a [scalar](https://spec.graphql.org/October2021/#sec-Scalars)) or a set of fields (an [object](https://spec.graphql.org/October2021/#sec-Objects)). While you naturally introduce new types for your own problem domain, there are few special types (called [Operations](https://spec.graphql.org/October2021/#sec-Language.Operations)). One of theses is [Query](https://spec.graphql.org/October2021/#sec-Query), which is the root of requests for data (setting aside [Subscription](https://spec.graphql.org/October2021/#sec-Subscription) for now, for the sake of simplicity). A type essentially is a set of rules for determining if a piece of data&#x2013;or a request for that piece of data&#x2013;validly conforms to the given type. A GraphQL type is very much like a user-defined type in programming languages like C++, Java, and Typescript, and is very much like a table in a relational database.
-   **field:** A field within *one* type contains one or more pieces of data that validly conform to *another* type, thus establishing *relationships* among types. A GraphQL field is very much like a property of a user-defined type in a programming language, and is very much like a column in a relational database. Relationships between GraphQL types are very much like pointers or references in programming languages, and are very much like foreign key constraints in relational databases.

There's more to GraphQL, but that's pretty much the essence. Note the similarities between concepts in GraphQL and in programming languages, and especially between concepts in GraphQL and in relational databases.

OK, that's enough for now about what GraphQL *is*, but what is GraphQL *for*? Why should we consider GraphQL, especially as an alternative to REST? I listed above some of GraphQL's improvements over typical REST&#x2013;expressivity, efficiency, discoverability, simplicity&#x2013;but another perhaps more concise way to put it is this:

> GraphQL's expressivity, efficiency, discoverability, and simplicity make life easier for data consumers.

However, there's a corollary:

> GraphQL's expressivity, efficiency, discoverability, and simplicity make life harder for data producers.

That's *you*! If you're a Java programmer working with GraphQL, your job is probably to *produce* GraphQL API servers for clients to *consume* (there are relatively few&#x2013;not "none", but "few"&#x2013;settings for Java on the client). Offering all that expressivity, discoverability, etc. ain't easy, so how do you do it?


<a id="org3940bb5"></a>

# How do I provide the GraphQL that people want, especially as a Java developer?

On the journey to providing a GraphQL API we confront a series interdependent choices, which *can* make life easier (or harder) for data producers. One choice is over just *how* "expressive, efficient, discoverable, and simple" is our API, but let's set that aside for a moment and treat that as an emergent property of the other choices we make. Life is about trade-offs, after all.

Another choice is over [build-versus-buy [PDF]​](https://www.thoughtworks.com/content/dam/thoughtworks/documents/e-book/tw_ebook_build_vs_buy_2022.pdf), but let's also set that aside for a moment, accept that we're building a GraphQL API server (in Java), explore how that is done, and evaluate the consequences.

If we're building a GraphQL API server in Java, another choice is over whether to build it completely from scratch or to use libraries and frameworks, and if the latter then which libraries and frameworks to use. Let's set *that* aside, rightfully regard a complete [DIY](https://en.wikipedia.org/wiki/Do_it_yourself) solution as pointless masochism, and survey the landscape of Java libraries and frameworks for GraphQL. As of writing (April 2024) there are three important interdependent players in this space:

-   **graphql-java:** [graphql-java](https://www.graphql-java.com/) is a lower-level foundational library for working with GraphQL in Java, which began in 2015. Since the other players depend on and use graphql-java, consider graphql-java to be *non-optional*. Another crucial choice is whether you are or are not using the [Spring Boot](https://spring.io/projects/spring-boot) framework. If you're *not* using Spring Boot then *stop here!* 🛑 Since this is a prerequisite, in the parlance of the [ThoughtWorks Radar](https://www.thoughtworks.com/radar) this is unavoidably **Adopt**.
-   **Netflix DGS:** [DGS](https://netflix.github.io/dgs/) is a higher-level library for working with GraphQL in Java *with Spring Boot*, which began in 2021. If you're using DGS then you *will* also be using graphql-java under-the-hood, but typically you won't come into contact with graphql-java. Instead, you will be sprinkling [annotations](https://en.wikipedia.org/wiki/Java_annotation) throughout the Java code to identify the code segments (called "resolvers" or "data fetchers"&#x2026;more on that [later](#org8d6ef13)) that execute GraphQL requests. Thoughtworks [said](https://www.thoughtworks.com/radar/languages-and-frameworks/netflix-dgs) **Trial** as of 2023 for DGS but this is a dynamic space and their opinion may have changed. I say **Hold**, for reasons given below.
-   **Spring for GraphQL:** [Spring for GraphQL](https://spring.io/projects/spring-graphql) is *another* higher-level library for working with GraphQL in Java with Spring Boot, which began around 2023, and is also based on annotations. It may be too new for ThoughtWorks, but it's not too new for me. I say **adopt**, and read on for why.

The makers of Spring for GraphQL [say](https://spring.io/projects/spring-graphql):

> It is a joint collaboration between the GraphQL Java team and Spring engineering&#x2026;It aims to be the foundation for all Spring, GraphQL applications.

Translation:

> The Spring team has a privileged collaboration with the makers of the foundational library for GraphQL in Java, and intends to "win" in this space.

Moreover, the makers of Netflix DGS have much to [say](https://netflix.github.io/dgs/spring-graphql-integration/) on the subject of that library's relationship to Spring for GraphQL.

> Soon after we open-sourced the DGS framework, we learned about parallel efforts by the Spring team to develop a GraphQL framework for Spring Boot. The Spring GraphQL project was in the early stages at the time and provided a low-level of integration with graphql-java. Over the past year, however, Spring GraphQL has matured and is mostly at feature parity with the DGS Framework. We now have 2 competing frameworks that solve the same problems for our users.
> 
> Today, new users must choose between the DGS Framework or Spring GraphQL, thus missing out on features available in one framework but not the other. This is not an ideal situation for the GraphQL Java community.
> 
> For the maintainers of DGS and Spring GraphQL, it would be far more effective to collaborate on features and improvements instead of having to solve the same problem independently. Finally, a unified community would provide us with better channels for feedback.
> 
> The DGS framework is widely used and plays a vital role in the architecture of many companies, including Netflix. Moving away from the framework in favor of Spring-GraphQL would be a costly migration without any real benefits.
> 
> From a Spring Framework perspective, it makes sense to have an out-of-the-box GraphQL offering, just like Spring supports REST.

Translation:

> If you're a Spring Boot shop already using DGS, go ahead and keep using it for now. If you're a Spring Boot shop starting afresh, you should probably just use Spring for GraphQL.

In this guide I've told you what GraphQL *is*. I've told you what GraphQL *really* is. I've set the stage by giving some background on the relevant libraries and frameworks in Java. Now, let me show you how to implement state-of-the-art GraphQL in Java for a real application, and since we're starting afresh we'll take the advice from DGS and just use Spring for GraphQL.


<a id="org9f0d658"></a>

# How exactly do I build a GraphQL API server in Java for a real application?

Opinions are free to differ on what it even means to be a "real application." For the purpose of this guide, what *I* mean by "real application" in this settings is an application that has at least these features:

-   **persistence:** Many [tutorials](https://www.graphql-java.com/tutorials/getting-started-with-spring-boot), [getting-started guides](https://netflix.github.io/dgs/), and [overviews](https://docs.spring.io/spring-graphql/reference/) only address in-memory data models, stopping well short of interacting with a database. This guide shows you *some* ways to cross this crucial chasm and discusses *some* of the consequences, challenges, and trade-offs involved. This is a vast topic so I barely scratch the surface, but it's a start. The primary goal is to support `Query` operations. A stretch goal is to support `Mutation` operations. `Subscription` operations are thoroughly off-the-table for now.
-   **flexibility:** I wrote above that just *how* expressive, efficient, discoverable, and simple we make our GraphQL API is technically a choice we make, but is practically a property that emerges from other choices we make. I also wrote that building GraphQL API servers is difficult for data producers. Consequently, many data producers cope with that difficulty by dialing way back on those other properties of the API. Many GraphQL API servers in the real world are inflexible, are superficial, are shallow, and are in many ways "GraphQL-in-name-only." This guide shows *some* of what's involved in going beyond the *status quo* and how that comes into tension with other properties, like efficiency. **Spoiler Alert**: It isn't pretty.
-   **efficiency:** In fairness, many GraphQL API servers in the real world achieve decent efficiency, albeit at the expense of flexibility, by essentially encoding REST API endpoints into a shallow GraphQL schema. The standard approach in GraphQL is the [data-loader pattern](https://www.graphql-java.com/documentation/batching/), but few tutorials really show how this is used even with an in-memory data model let alone with a database. This guide offers one implementation of the data loader pattern to combat the N+1 problem. Again, we see how that comes into tension with flexibility and simplicity.
-   **modernity:** Anyone writing a Java application that accesses a database will have to make choices about *how* to access a database. That could involve just [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity) and raw SQL (for a relational database) but arguably the current industry standard is still to use an Object-Relational Mapping ([ORM](https://web.archive.org/web/20220823105749/http://blogs.tedneward.com/post/the-vietnam-of-computer-science/)) layer like [Hibernate](https://hibernate.org/orm/), [jooq](https://www.jooq.org/), or the standard [JPA](https://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html). Getting an ORM to play nice with GraphQL is a tall order, may not be prudent, and may not even be possible. Few if any other guides touch this with a ten-foot-pole. This guide at least ~~makes an attempt~~ *will make an attempt with an ORM in the future!*

The recipe I follow in this guide for building a GraphQL API server in Java *for a relational database* is the following:

1.  Choose [Spring Boot](https://spring.io/projects/spring-boot) for the overall server framework.
2.  Choose [Spring for GraphQL](https://spring.io/projects/spring-graphql) for the GraphQL-specific parts.
3.  Choose [Spring Data for JDBC](https://spring.io/projects/spring-data-jdbc) for data access in lieu of an ORM *for now*.
4.  Choose [Maven](https://maven.apache.org/) over [Gradle](https://gradle.org/) because I prefer the former. If you choose the latter, you're on your own.
5.  Choose [PostgreSQL](https://www.postgresql.org/) for the database. Most of the principles should apply for pretty much any relational database, but you've got to start somewhere.
6.  Choose [Docker Compose](https://docs.docker.com/compose/) for orchestrating a development database server. There are [other](https://testcontainers.com/) ways of bringing in a database, but again, you've got to start somewhere.
7.  Choose the [Chinook](https://docs.yugabyte.com/preview/sample-data/chinook) data model. Naturally, you will have your own data model, but Chinook is a good choice for illustration purposes because it's fairly rich, has quite a few tables and relationships, goes well beyond the ubiquitous but trivial [To-Do](https://todomvc.com/) apps, is available for a wide variety of databases, and is generally well-understood.
8.  Choose the [Spring Initializr](https://netflix.github.io/dgs/#create-a-new-spring-boot-application) for bootstrapping the application. There's so much ceremony in Java, any way to race through some of it is welcomed.
9.  [Create](https://netflix.github.io/dgs/#creating-a-schema) a GraphQL schema file. This is a necessary step for graphql-java, for DGS, and for Spring for GraphQL. Weirdly, the Spring for GraphQL overview seems to overlook this step, but the DGS "Getting Started" guide is there to remind us. Many "thought leaders" will exhort you to isolate your underlying data model from your API. Theoretically, you could do this by having different GraphQL types from your database tables. Practically, this is a source of busy-work.
10. Write Java model classes, one for every GraphQL type in the schema file and every table in the database. You're free to make other choices for this data model or for any other data model, and you can even write code or SQL views to isolate your underlying data model from your API, but do ask how important this really is when the number of tables/classes/types grows to the hundreds or thousands.
11. Write Java controller classes, with one method at least for every [root field](https://www.apollographql.com/tutorials/fullstack-quickstart/04-writing-query-resolvers). In practice, this is the bare minimum. There probably will be many more. By the way, these methods *are* your "resolvers".
12. Annotate every controller class with `@Controller` to tell Spring to inject it as a Java Bean that can serve network traffic.
13. Annotate every resolver/data-fetcher method with `@SchemaMapping` or `QueryMapping` to tell Spring for GraphQL how to execute the parts of a GraphQL operation.
14. Implement those resolver/data-fetcher methods *by whatever means necessary* to mediate interactions with the database. In version 0, this will be just simple raw SQL statements.
15. Upgrade *some* of those resolver/data-fetcher methods by replacing `@SchemaMapping` or `@QueryMapping` with `@BatchMapping`. This latter annotation signals to Spring for GraphQL that we want make the execution more efficient by combating the N+1 problem, and we're prepared to pay the price in more code in order do do it.
16. Refactor those `@BatchMapping`-annotated methods to support the data loader pattern, by accepting (and processing) a *list* of identifiers for related entities, rather than a single identifier for a single related entity.
17. ~~Write copious test-cases for every possible interaction.~~
18. Just use a [fuzz-tester](https://github.com/EMResearch/EvoMaster) on the API and call it a day.


<a id="orgab05048"></a>

# No, but really, how exactly do I build a GraphQL API server in Java for a real application?

That is a *long recipe* above! Instead of going into chapter-and-verse for every single step, in this guide I do two things. First, I provide a public [repository](https://github.com/dventimihasura/graphql-with-java) with working code that is easy to use, easy to run, easy to read, and easy to understand. If you feel it falls short in any of these objects *please do let me know!* Second, I highlight *some* of the important steps, put them in context, discuss the choices involved, and offer some alternatives.


<a id="org01562ef"></a>

## Step 6:  Choose [Docker Compose](https://docs.docker.com/compose/) for orchestrating a development database server.

Again, there are other ways to pull this off, but this is one good way.

```yaml
version: "3.6"
services:
  postgres:
    image: postgres:16
    ports:
      - ${PGPORT:-5432}:5432
    restart: always
    environment:
      POSTGRES_PASSWORD: postgres
      PGDATA: /var/lib/pgdata
    volumes:
      - ./initdb.d-postgres:/docker-entrypoint-initdb.d:ro
      - type: tmpfs
        target: /var/lib/pg/data
```

Set an environment variable for `PGPORT` to expose PostgreSQL on a host port, or hard-code it to whatever value you like.

Start the server with:


<a id="orge37c2ff"></a>

## Step 7:  Choose the [Chinook](https://www.yugabyte.com/blog/postgresql-how-to-installing-the-chinook-sample-db-on-a-distributed-sql-database/) data model.

The Chinook files from [YugaByte](https://www.yugabyte.com/blog/postgresql-how-to-installing-the-chinook-sample-db-on-a-distributed-sql-database/) work out-of-the-box for PostgreSQL and are a good choice. Just make sure that there is a sub-directory `initdb.d-postgres` and download the Chinook DDL and DML files into that directory, taking care to give them numeric prefixes so that they're run by the PostgreSQL initialization script in the proper order.

```shell
mkdir -p ./initdb.d-postgres
wget -O ./initdb.d-postgres/04_chinook_ddl.sql
wget -O ./initdb.d-postgres/05_chinook_genres_artists_albums.sql
wget -O ./initdb.d-postgres/06_chinook_songs.sql
```

Now, you can start the database service using Docker Compose.

```shell
docker compose up -d
```

or

```shell
docker-compose up -d
```

There are many ways to spot check the validity of the database. If the Docker Compose service seems to have started correctly, here's one way using `psql`.

```shell
psql "postgresql://postgres:postgres@localhost:5432/postgres" -c '\d'
```

```
              List of relations
 Schema |      Name       | Type  |  Owner   
--------+-----------------+-------+----------
 public | Album           | table | postgres
 public | Artist          | table | postgres
 public | Customer        | table | postgres
 public | Employee        | table | postgres
 public | Genre           | table | postgres
 public | Invoice         | table | postgres
 public | InvoiceLine     | table | postgres
 public | MediaType       | table | postgres
 public | Playlist        | table | postgres
 public | PlaylistTrack   | table | postgres
 public | Track           | table | postgres
 public | account         | table | postgres
 public | account_summary | view  | postgres
 public | order           | table | postgres
 public | order_detail    | table | postgres
 public | product         | table | postgres
 public | region          | table | postgres
(17 rows)

```

You should at least see Chinook-specific tables like `Album`, `Artist`, and `Track`.


<a id="org901daeb"></a>

## Step 8:  Choose the [Spring Initializr](https://start.spring.io/) for bootstrapping the application.

The important things with this form are to make these choices:

-   **Project:** Maven
-   **Language:** Java
-   **Spring Boot:** 3.2.5
-   **Packaging:** Jar
-   **Java:** 21
-   **Dependencies:** -   Spring for GraphQL
    -   PostgreSQL Driver

You can make other choices (e.g. Gradle, Java 22, MySQL, etc.) but bear in mind that this guide has only been tested with the choices above.


<a id="orgf41b657"></a>

## Step 9: [Create](https://netflix.github.io/dgs/#creating-a-schema) a GraphQL schema file.

Maven projects have a standard directory layout, and a standard place within that layout for resource files to be packaged into the build artifact (a JAR file) is `./src/main/java/resources`. Within that directory, create a sub-directory `graphql` and deposit a `schema.graphqls` file. There are other ways to organize the GraphQL schema files needed by graphql-java, DGS, and Spring for GraphQL, but they all are rooted in `./src/main/java/resources` (for a Maven project).

Within the `schema.graphqls` file (or its equivalent), first there will a definition for the root `Query` object, with root-level fields for every GraphQL type that we want in our API. As a starting point, there will be a root-level field under `Query` for every table, and a corresponding `type` for every table. For example, for `Query`:

```graphql
type Query {
  Artist(limit: Int): [Artist]
  ArtistById(id: Int): Artist
  Album(limit: Int): [Album]
  AlbumById(id: Int): Album
  Track(limit: Int): [Track]
  TrackById(id: Int): Track
  Playlist(limit: Int): [Playlist]
  PlaylistById(id: Int): Playlist
  PlaylistTrack(limit: Int): [PlaylistTrack]
  PlaylistTrackById(id: Int): PlaylistTrack
  Genre(limit: Int): [Genre]
  GenreById(id: Int): Genre
  MediaType(limit: Int): [MediaType]
  MediaTypeById(id: Int): MediaType
  Customer(limit: Int): [Customer]
  CustoemrById(id: Int): Customer
  Employee(limit: Int): [Employee]
  EmployeeById(id: Int): Employee
  Invoice(limit: Int): [Invoice]
  InvoiceById(id: Int): Invoice
  InvoiceLine(limit: Int): [InvoiceLine]
  InvoiceLineById(id: Int): InvoiceLine
}
```

Note the parameters on these fields. I have written it so that every root-level field that has a [List](https://spec.graphql.org/October2021/#sec-Wrapping-Types) return type accepts one optional `limit` parameter which accepts an `Int`. The intention is to support limiting the number of entries that should be returned from a root-level field. Note also that every root-level field that has a [Scalar](https://spec.graphql.org/October2021/#ScalarTypeDefinition) object return type accepts one optional `id` parameter which also accepts an `Int`. The intention is to support fetching a single entry by its identifier (which happen all to be `integer` primary keys in the Chinook data model).

Next, here is an illustration of *some* of the corresponding GraphQL types:

```graphql
type Album {
  AlbumId  : Int
  Title    : String
  ArtistId : Int
  Artist   : Artist
  Tracks   : [Track]
}

type Artist {
  ArtistId: Int
  Name: String
  Albums: [Album]
}

type Customer {
  CustomerId   : Int
  FirstName    : String
  LastName     : String
  Company      : String
  Address      : String
  City         : String
  State        : String
  Country      : String
  PostalCode   : String
  Phone        : String
  Fax          : String
  Email        : String
  SupportRepId : Int
  SupportRep   : Employee
  Invoices     : [Invoice]
}
```

Fill out the rest of the `schema.graphqls` file as you see fit, exposing whatever table (and possibly views, if you create them) you like. Or, just use the complete version from the shared repository.


<a id="org59aab0c"></a>

## Step 10:  Write Java model classes.

Within the standard Maven directory layout, Java source code goes into `./src/main/java` and its sub-directories. Within an appropriate sub-directory for whatever Java package you use, create Java model classes. These can be Plain Old Java Objects ([POJOs](https://en.wikipedia.org/wiki/Plain_old_Java_object)). They can be Java [Record](https://docs.oracle.com/en/java/javase/17/language/records.html) classes. They can be whatever you like, so long as they have "getter" and "setter" property methods for the corresponding fields in the GraphQL schema. In this guide's repository, I choose Java Record classes just for the minimal amount of boilerplate.

```java
  package com.graphqljava.tutorial.retail.models;

  public class ChinookModels {
      public static
          record Album
          (
           Integer AlbumId,
           String Title,
           Integer ArtistId
           ) {}

      public static
          record Artist
          (
           Integer ArtistId,
           String Name
           ) {}

      public static
          record Customer
          (
           Integer CustomerId,
           String FirstName,
           String LastName,
           String Company,
           String Address,
           String City,
           String State,
           String Country,
           String PostalCode,
           String Phone,
           String Fax,
           String Email,
           Integer SupportRepId
           ) {}
  ...
}
```


<a id="org169a569"></a>

## Step 11-14:  Write Java controller classes.  Annotate every controller.  Annotate every resolver/data-fetcher.  Implement those resolver/data-fetcher.

These are the Spring `@Controller` classes, and within them are the Spring for GraphQL `QueryMapping` and `@SchemaMapping` resolver/data-fetcher methods. These are the real workhorses of the application, accepting input parameters, mediating interaction with the database, validating data, implementing (or delegating) to business logic code segments, arranging for SQL and DML statements to be sent to the database, returning the data, processing the data, and sending it along to the GraphQL libraries (graphql-java, DGS, Spring for GraphQL) to package up and send off to the client. There are *so* many choices one can make in implementing these and I can't go into every detail. Let me just illustrate how *I* have done it, highlight some things to look out for, and discuss some of the options that are available.

For reference, we will look at a section of the `ChinookControllers` file from the example repository.

```java
package com.graphqljava.tutorial.retail.controllers; // It's got to go into a package somewhere.

import java.sql.ResultSet;	// There's loads of symbols to import.
import java.sql.SQLException;	// This is Java and there's no getting around that.
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
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


public class ChinookControllers { // You don't have to nest all your controllers in one file. It's just what I do.
    @Controller public static class ArtistController { // Tell Spring about this controller class.
        @Autowired JdbcClient jdbcClient; // Lots of ways to get DB access from the container.  This is one way in Spring Data.
        RowMapper<Artist>		  // I'm not using an ORM, and only a tiny bit of help from Spring Data.
            mapper = new RowMapper<>() {  // Consequently, there are these RowMapper utility classes involved.
                    public Artist mapRow (ResultSet rs, int rowNum) throws SQLException {
                        return
                        new Artist(rs.getInt("ArtistId"),
                                   rs.getString("Name"));}};
        @SchemaMapping Artist Artist (Album album) { // @QueryMapping when we can, @SchemaMapping when we have to
            return				     // Here, we're getting an Artist for a given Album.
                jdbcClient
                .sql("select * from \"Artist\" where \"ArtistId\" = ? limit 1") // Simple PreparedStatement wrapper
                .param(album.ArtistId()) // Fish out the relating field ArtistId and pass it into the PreparedStatement
                .query(mapper)		 // Use our RowMapper to turn the JDBC Row into the desired model class object.
                .optional()		 // Use optional to guard against null returns!
                .orElse(null);}
        @QueryMapping(name = "ArtistById") Artist // Another resolver, this time to get an Artist by its primary key identifier
            artistById (ArgumentValue<Integer> id) { // Note the annotation "name" parameter, when the GraphQL field name doesn't match exactly the method name
            for (Artist a : jdbcClient.sql("select * from \"Artist\" where \"ArtistId\" = ?").param(id.value()).query(mapper).list()) return a;
            return null;}
        @QueryMapping(name = "Artist") List<Artist> // Yet another resolver, this time to get a List of Artists.
            artist (ArgumentValue<Integer> limit) { // Note the one "limit" parameter.  ArgumentValue<T> is the way you do this with GraphQL for Java.
            StatementSpec
                spec = limit.isOmitted() ? // Switch SQL on whether we did or did not get the limit parameter.
                jdbcClient.sql("select * from \"Artist\"") :
                jdbcClient.sql("select * from \"Artist\" limit ?").param(limit.value());
            return		// Run the SQL, map the results, return the List.
                spec
                .query(mapper)
                .list();}}
...
```

There's a lot to unpack here, so let's go through it step by step. First, I included the `package` and `import` statements in the example because all too often, tutorials and guides that you find online elide these details for brevity. The problem with that, however, is that it's *not compilable or runnable code*. You don't know where these symbols are coming from, what packages they're in, and what libraries they're coming from. Any decent editor like IntelliJ, VSCode, or even Emacs will help sort this out for you *when you're writing code*, but you don't have that when reading a blog article. Moreover, there *can* be name conflicts and ambiguities among symbols across libraries, so even with a smart editor it can leave the reader scratching their head.

Next, please forgive the nested inner classes. Feel free to explode your classes out into their own individual files as you see fit. This is just how I do it, largely for pedagogical purposes like this one, to promote [Locality of Behavior](https://htmx.org/essays/locality-of-behaviour/), which is just a fancy way of saying, "let's not make the reader have to jump through a lot of hoops to understand the code."

Now for the meat of the code. Aside from niggling details like "How do I get a database connection", "How do I map data", etc., the patterns I want you to see through the forest of code are these:

1.  Every field in our schema file (`schema.graphqls`) which isn't a simple scalar field (e.g., `Int`, `String`, `Boolean`) probably will need a resolver/data-fetcher.
2.  Every resolver is implemented with a Java method.
3.  Every resolver method gets annotated with `@SchemaMapping`, `@QueryMapping`, or `@BatchMapping` (more on that [later](#org37a798d)).
4.  Use `@QueryMapping` when you can because it's simpler. Use `@SchemaMapping` when you have to (your IDE should nag you).
5.  If you keep the Java method names in sync with the GraphQL field names, it's a little less code, but don't make a federal case out of it. You can fix it with a `name` parameter in the annotations.
6.  Unless you do something different (such as adding filtering, sorting, and pagination), you probably will be fetching either a single entry by its primary key, or a list of entries. You *won't* be fetching "child" entries; that's handled by the GraphQL libraries and the recursive divide-and-conquer way they process GraphQL operations. **Note**: This has implications for performance, efficiency, and code complexity.
7.  The "something different" in the above item refers to richness that you want to add to your GraphQL API. Want `limit` operations? Filter predicates? Aggregations? Supporting those cases will involve more `ArgumentValue<>` parameters, more `SchemaMapping` resolver methods, and more combinations thereof. Deal with it.
8.  You *will* experience the urge to be clever, to create abstractions that dynamically respond to more and more complex combinations of parameters, filters, and other conditions. Congratulations: you're on your way to building a [general-purpose query engine](#org8d6ef13).


<a id="org37a798d"></a>

## Step 15:  Upgrade *some* of those resolver/data-fetcher methods with the data loader pattern.

You will quickly realize that this can lead to overly chatty interaction with the database, sending too many small SQL statements and impacting performance and availability. This is the proverbial "N+1" problem.

In a nutshell, the N+1 problem can be illustrated by our Chinook data model. Suppose we have this GraphQL query.

```graphql
query {
  Artist(limit: 10) {
    ArtistId
    Album {
      AlbumId
      Track {
        TrackId
      }
    }
  }
}
```

1.  Get up to 10 `Artist` entry.
2.  For each `Artist`, get all of the related `Album` entries.
3.  For each `Album`, get all of the related `Track` entries.
4.  For each entry, just get its identifier field: `ArtistId`, `AlbumId`, `TrackId`.
5.  This query is nested 2 levels below `Artist`. Let $~n=2~$.
6.  `Album` is a [List](https://spec.graphql.org/October2021/#sec-Wrapping-Types) wrapping type on `Artist`, as is `Track` is a [List](https://spec.graphql.org/October2021/#sec-Wrapping-Types) wrapping type on `Album`. Suppose the typical [cardinality](https://en.wikipedia.org/wiki/Cardinality) is $m$.

How many SQL statements will typically be involved

1.  1 to fetch 10 `Artist` entries.
2.  $10*m$ to fetch the `Album` entries.
3.  $10*m^2$ to fetch the `Track` entries.

In general, we can see that the number of queries scales as $m^n$, which is exponential in $n$. Of course, observe that the amount of data retrieved also scales as $m^n$. In any case, on its face, this *seems* like an alarmingly inefficient way to go about fetching these data. Is there another way?

There is another way and it it is the standard answer within the GraphQL community for combating this N+1 problem: the data loader pattern (aka "batching"). This encompasses two ideas:

1.  Rather than fetch the related child entities (e.g. `Album`) for a *single* parent entity (e.g. `Artist`) using one identifier, fetch the related entities for *all* of the parent entities in one go, using a list of identifiers.
2.  Group the resulting child entities according to their respective parent entities (in code).
3.  While we're at it, we might as well cache the entities for the lifetime of executing the one GraphQL operation, in case a given entity appears in more than one place in the graph.

Now, for some code. Here's how this looks in our example.

```java
@BatchMapping(field = "Albums") public Map<Artist, List<Album>> // Switch to @BatchMapping
    albumsForArtist (List<Artist> artists) { // Take in a List of parents rather than a single parent
    return
        jdbcClient
        .sql("select * from \"Album\" where \"ArtistId\" in (:ids)") // Use a SQL "in" predicate taking a list of identifiers
        .param("ids", artists.stream().map(x -> x.ArtistId()).toList()) // Fish the list of identifiers out of the list of parent objects
        .query(mapper)	// Can re-use our usual mapper
        .list()
        .stream().collect(Collectors.groupingBy(x -> artists.stream().collect(Collectors.groupingBy(Artist::ArtistId)).get(x.ArtistId()).getFirst()));
    // ^ Java idiom for grouping child Albums according to their parent Albums
}
```

Like before, let's unpack this. First, we switch from either the `@QueryMapping` or `@SchemaMapping` annotation to `@BatchMapping`, to signal to Spring for GraphQL that we want to use the data loader pattern. Second, we switch from a single `Artist` parameter to a `List<Artist>` parameter. Third, we somehow have to arrange the necessary SQL (with an `in` predicate in this case) and the corresponding parameter (a `List<Integer>` extracted from the `List<Album>` parameter). Fourth, we have somehow have to arrange for the child entries (`Album` in this case) to get sorted to the right parent entries (`Album` in this case). There are many ways to do it, and this is just one way. The important point is that however it's done, *it has to be done in Java*. One last thing: note the absence of the `limit` parameter. Where did that go? It turns out that `InputValue<T>` is [not supported](https://github.com/spring-projects/spring-graphql/issues/232#issuecomment-1071044083) by Spring for GraphQL for `@BatchMapping`. Oh well! 😒 In this case, it's no great loss because arguably these `limit` parameters make little sense. How often does one really need a random subset of an artist's albums? It's a more serious issue if we had filtering and sorting, however. Filtering and sorting parameters are more justified, and if we had them we would somehow have to find a way to sneak them into the data loader pattern. Presumably, it can be done, but it will not be so easy as just slapping a `@BatchMapping` annotation onto the method and tinkering with [Java streams](https://www.oracle.com/technical-resources/articles/java/ma14-java-se-8-streams.html).


<a id="orgac6576c"></a>

### Editorial Aside!

> This raises an important point about the "N+1 problem" that is *never* addressed, and that neglect just serves to exaggerate the scale of the problem in a real world setting. *If* we have limits and/or filtering, then we have a way of reducing the cardinality of related child entities below $m$ (recall that we took $m$ to be the typical cardinality of a child entity). In a real world setting *limits or more precisely filtering are necessary for usability*. GraphQL APIs are meant for humans, in that at the end of the day, the data are being painted onto a screen or in some other way presented to a human user who then has to absorb and process those data. Humans have severe limits in perception, cognition, and memory, for the quantity of data we can process. Only another machine (i.e. computers) could possibly process a large volume of data, but if you're extracting large volumes of data from one machine to another, then you are building an [ETL](https://en.wikipedia.org/wiki/Extract,_transform,_load) pipeline. If you are using GraphQL for ETL then *you are doing it wrong* and should stop immediately!
> 
> In any event, in a real world setting, with human users, both $m$ and $n$ will be very small. The number of SQL queries *will not* scale as $m^n$ to very large numbers. Effectively, the N+1 problem will inflate the number of SQL queries not by an *arbitrarily large factor*, but by approximately a *constant factor*. In a well-designed application, it probably will be a constant factor well below 100. Consider this when balancing the trade-offs in developer time, in complexity, and in hardware scaling, when confronting the N+1 problem.


<a id="org8d6ef13"></a>

# Is this the *Only* way to build a GraphQL API server?

We saw that the "easy way" of building GraphQL servers is the one typically offered in tutorials and "Getting Started" guides, and is over tiny unrealistic in-memory data models, without a database.

We saw that the "real way" of building GraphQL servers (in Java) described in some detail above, regardless of library or framework, involves:

-   writing schema file entries, possibly for every table
-   writing Java model classes, possibly for every table
-   writing Java resolver methods, possibly for every field in every table
-   eventually writing code to solve arbitrarily complex compositions of input parameters
-   writing code to budget SQL operations efficiently

We also observe that GraphQL lends itself to a "recursive [divide-and-conquer](https://en.wikipedia.org/wiki/Divide-and-conquer_algorithm) with an accumulator approach": a GraphQL query is recursively divided and sub-divided along type and field boundaries into a "graph", internal nodes in the graph are processed individually by resolvers, but the data are passed up the graph [dataflow](https://en.wikipedia.org/wiki/Dataflow_programming) style, accumulating into a JSON envelope that is returned to the user. The GraphQL libraries are decomposing the incoming queries into something like an Abstract Syntax Tree ([AST](https://en.wikipedia.org/wiki/Abstract_syntax_tree)), firing SQL statements for all the internal nodes (ignoring the data loader pattern for a moment), then re-composing the data. And, we are its willing accomplices!

We *also* observe that building GraphQL servers according to the above recipes leads to other outcomes:

-   lots of repetition
-   lots of boilerplate code
-   bespoke servers
-   that are tied to a particular data model

Build a GraphQL server more than once according to the above recipes and you will make these observations. Making these observations, you will naturally feel a powerful urge to build more sophisticated abstractions that reduce the repetition, reduce the boilerplate, generalize the servers, and decouple them from any particular data model. This is what I call the "natural way" of building a GraphQL API, as it's a natural evolution from the trivial "easy way" of tutorials and "Getting Started" guides, and from the cumbersome "real way" of resolvers and even data loaders. More generally, it can be considered the [Domain-Driven](https://hasura.io/blog/compile-dont-resolve-designing-a-feature-rich-high-performance-domain-driven-graphql-api) approach, which [Praveen Durairaju](https://hasura.io/blog/@praveenweb) describes in this way.

> At its core, the domain-driven approach translates the richness of your domain, primarily the storage layer (databases), to your API. Databases have been around for a while now and they are increasingly getting more and more powerful. SQL, regardless of the specific dialect supported by your SQL DB, has long been the gold standard of expressiveness of data requirements – any sufficiently expressive API will begin to look like SQL – there’s no alternative. So, instead of reinventing the wheel, a domain-driven approach mirrors whatever flexibility your domain allows in terms of access patterns/capabilities.

Building a GraphQL server with a network of nested resolvers offers some flexibility and dynamism, and requires a lot of code. Adding in more flexibility and dynamism with limits, pagination, filtering, and sorting, requires more code still. And while it may be dynamic, it will also be very chatty with the database, as we saw. Reducing the chattiness necessitates composing the many fragmentary SQL statements into fewer SQL statements which individually do more work. That's what the data loader pattern does: it reduces the number of SQL statements from "a few tens" to "less than 10 but more than 1". In practice, that may not be a huge win and it comes at the cost of developer time and lost dynamism, but it *is* a step down the path of generating fewer, more sophisticated queries. The terminus of that path is "1": the optimal number of SQL statements (ignoring caching) is 1. Generate one giant SQL statement that does *all* the work of fetching the data, teach it to generate JSON while you're at it, and this is the best you will ever do with a GraphQL server (for a relational database). It will be hard work, but you can take solace that having done it once, it need not ever be done again if you do it right, by introspecting the database to *generate* the schema. Do *that*, and what you will build won't be so much a "GraphQL API server" as a "GraphQL to SQL compiler." [Praveen](https://hasura.io/blog/@praveenweb) goes on to say in [Compile, don't resolve: Designing a feature-rich, high-performance, domain-driven GraphQL API](https://hasura.io/blog/compile-dont-resolve-designing-a-feature-rich-high-performance-domain-driven-graphql-api) on the following on this topic:

> don’t resolve GraphQL requests&#x2026;[instead] compile them to a “language” understood by the upstream data source – a SQL query or a REST call. If you use multiple type systems (GraphQL, SQL, OpenAPI Spec, etc.), why not leverage these systems to interoperate efficiently?

If that gives you pause, consider that this is what you were already doing all along, anyway. ~~The easy way~~, the real way, the natural way: they're ~~all~~ both necessarily GraphQL to SQL compilers! They just lie along a spectrum of versatility, flexibility, and efficiency. Acknowledge that building a GraphQL to SQL compiler is what you were doing all along, embrace that fact, and lean into it, and you may never need to build another GraphQL server again. What could be better than that?

One thing that could be better than building your last GraphQL server, or your only GraphQL server, is never building a GraphQL server in the first place. After all, your goal wasn't to *build* a GraphQL API, but rather to *have* a GraphQL API. The easiest way to *have* a GraphQL API is just to go get one. Get one for free if you can. Buy one if the needs justify it. This is the [final boss](https://en.wikipedia.org/wiki/Boss_(video_games)#Final_boss) on the [journey](https://dev.to/sandipd/from-basics-to-supergraph-a-practical-guide-for-your-graphql-adoption-journey-5cff) of GraphQL maturity.


<a id="org3f236d3"></a>

# How to choose "Buy" over "Buy"

Of course "buy" in this case is really just a stand-in for the general concept which is to "acquire" an existing solution rather than building one. If this path is available, there are several fine options, though as the constraints pile up the choices narrow quickly.

A fine self-hosted open-source option is [PostGraphile](https://www.graphile.org/postgraphile/), a GraphQL server for PostgreSQL. However, many operations will prefer a managed solution. Moreover, many others will require access to a diverse, heterogeneous set of data sources. This is where [Hasura](https://hasura.io/) really shines. Like PostGraphile, Hasura offers a self-hosted [open-source](https://hasura.io/opensource/) option for PostgreSQL, and it comes "batteries-included" with a lot of functional and non-functional concerns that you will never get around to with a DIY approach:

-   rich API
-   high performance
-   authentication and authorization
-   observability
-   business logic and data integration

Moving from self-hosted to the cloud, Hasura has got you covered there as well, with [Hasura Cloud](https://hasura.io/cloud/). It has a generous "free tier" and a smooth upgrade path and modest [pricing](https://hasura.io/pricing) for production workloads. Moving further up the ladder to large enterprise settings, the smooth upgrade path continues with [Hasura Enterprise](https://hasura.io/enterprise/). Moreover, with Hasura Cloud and Hasura Enterprise, it opens the door to many more data sources, including but not limited to:

-   PostgreSQL
-   MySQL/MariaDB
-   Microsoft SQL Server
-   Oracle
-   Snowflake
-   MongoDB

Hasura is a good option, but it's not the only option for "buy." Even "build" via a DIY approach still is an option, in Java as it is in other programming languages. If you do choose to build GraphQL servers with Java, I hope you will find this article&#x2013;and especially the section [above](#org37a798d)&#x2013;helpful in breaking out of the relentless tutorials, "Getting Started" guides, and "To-Do" apps. These are vast topics in a shifting landscape, which warrant an iterative approach, as well as a modest amount of repetition. We at Hasura will continue to repeat and refine these ideas&#x2013;and the details within&#x2013;as a leader in the GraphQL community. As we do we welcome input from and collaboration with the community. Feedback is always welcome, so if there are ways that this guide can be improved please do let us know.


<a id="org3e1ab65"></a>

# About Me

My name is David A. Ventimiglia. I have been a physicist, an educator, a software engineer, a data engineer, a machine learning engineer, and a solutions engineer. Today, I am a solutions architect at [Hasura](https://hasura.io/). I came here because Hasura embraces principles I have learned to live by over my career: simplicity over complexity, constraints over code, data over algorithms, people over process. For many common tasks in software design, architecture, and engineering, these principles will guide you to solutions faster, cheaper, and more reliably than conventional wisdom will allow. It's not difficult, but it does involve a change of perspective. Hasura the software can help you do it. Hasura the company can help you support it. And along the way, I can help you understand it.
