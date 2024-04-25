package com.graphqljava.tutorial.retail.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import com.graphqljava.tutorial.retail.models.ChinookModels.*;

public class ChinookControllers {
    @Controller
    public static class AlbumController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Album>
	    albumMapper = new RowMapper<>() {
		    public Album mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Album(
				  rs.getInt("AlbumId"),
				  rs.getString("Title"),
				  rs.getInt("ArtistId")
				  );}};
	@QueryMapping
	List<Album>
	    Album (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Album\"") :
		jdbcClient.sql("select * from \"Album\" limit ?").param(limit.value());
	    return
		spec
		.query(albumMapper)
		.list();}}
    @Controller
    public static class ArtistController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Artist>
	    artistMapper = new RowMapper<>() {
		    public Artist mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Artist(rs.getInt("ArtistId"),
				   rs.getString("Name"));}};
	@QueryMapping
	List<Artist>
	    Artist (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Artist\"") :
		jdbcClient.sql("select * from \"Artist\" limit ?").param(limit.value());
	    return
		spec
		.query(artistMapper)
		.list();}}
    @Controller
    public static class CustomerController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Customer>
	    customerMapper = new RowMapper<>() {
		    public Customer mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Customer(
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
				     );}};
	@QueryMapping
	List<Customer>
	    Customer (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Customer\"") :
		jdbcClient.sql("select * from \"Customer\" limit ?").param(limit.value());
	    return
		spec
		.query(customerMapper)
		.list();}}
    @Controller
    public static class EmployeeController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Employee>
	    employeeMapper = new RowMapper<>() {
		    public Employee mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Employee(
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
				     );}};
	@QueryMapping
	List<Employee>
	    Employee (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Employee\"") :
		jdbcClient.sql("select * from \"Employee\" limit ?").param(limit.value());
	    return
		spec
		.query(employeeMapper)
		.list();}}
    @Controller
    public static class GenreController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Genre>
	    genreMapper = new RowMapper<>() {
		    public Genre mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Genre(
				  rs.getInt("GenreId"),
				  rs.getString("Name")
				  );}};
	@QueryMapping
	List<Genre>
	    Genre (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Genre\"") :
		jdbcClient.sql("select * from \"Genre\" limit ?").param(limit.value());
	    return
		spec
		.query(genreMapper)
		.list();}}
    @Controller
    public static class InvoiceController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Invoice>
	    invoiceMapper = new RowMapper<>() {
		    public Invoice mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Invoice(
				    rs.getInt("InvoiceId"),
				    rs.getInt("CustomerId"),
				    rs.getString("InvoiceDate"),
				    rs.getString("BillingAddress"),
				    rs.getString("BillingCity"),
				    rs.getString("BillingState"),
				    rs.getString("BillingCountry"),
				    rs.getString("BillingPostalCode"),
				    rs.getFloat("Total")
				    );}};
	@QueryMapping
	List<Invoice>
	    Invoice (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Invoice\"") :
		jdbcClient.sql("select * from \"Invoice\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
    @Controller
    public static class InvoiceLineController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<InvoiceLine>
	    invoiceMapper = new RowMapper<>() {
		    public InvoiceLine mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new InvoiceLine(
					rs.getInt("InvoiceLineId"),
					rs.getInt("InvoiceId"),
					rs.getInt("TrackId"),
					rs.getFloat("UnitPrice"),
					rs.getInt("Quantity")
					);}};
	@QueryMapping
	List<InvoiceLine>
	    InvoiceLine (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"InvoiceLine\"") :
		jdbcClient.sql("select * from \"InvoiceLine\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
    @Controller
    public static class MediaTypeController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<MediaType>
	    invoiceMapper = new RowMapper<>() {
		    public MediaType mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new MediaType(
				      rs.getInt("MediaTypeId"),
				      rs.getString("Name")
				      );}};
	@QueryMapping
	List<MediaType>
	    MediaType (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"MediaType\"") :
		jdbcClient.sql("select * from \"MediaType\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
    @Controller
    public static class PlaylistController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Playlist>
	    invoiceMapper = new RowMapper<>() {
		    public Playlist mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Playlist(
				     rs.getInt("PlaylistId"),
				     rs.getString("Name")
				     );}};
	@QueryMapping
	List<Playlist>
	    Playlist (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Playlist\"") :
		jdbcClient.sql("select * from \"Playlist\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
    @Controller
    public static class PlaylistTrackController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<PlaylistTrack>
	    invoiceMapper = new RowMapper<>() {
		    public PlaylistTrack mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new PlaylistTrack(
					  rs.getInt("PlaylistId"),
					  rs.getInt("TrackId")
					  );}};
	@QueryMapping
	List<PlaylistTrack>
	    PlaylistTrack (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"PlaylistTrack\"") :
		jdbcClient.sql("select * from \"PlaylistTrack\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
    @Controller
    public static class TrackController {
	@Autowired JdbcClient jdbcClient;
	RowMapper<Track>
	    invoiceMapper = new RowMapper<>() {
		    public Track mapRow (ResultSet rs, int rowNum) throws SQLException {
			return
			new Track(
				  rs.getInt("TrackId"),
				  rs.getString("Name"),
				  rs.getInt("AlbumId"),
				  rs.getInt("GenreId"),
				  rs.getString("Composer"),
				  rs.getInt("Milliseconds"),
				  rs.getInt("Bytes"),
				  rs.getFloat("UnitPrice")
				  );}};
	@QueryMapping
	List<Track>
	    Track (ArgumentValue<Integer> limit) {
	    StatementSpec
		spec = limit.isOmitted() ?
		jdbcClient.sql("select * from \"Track\"") :
		jdbcClient.sql("select * from \"Track\" limit ?").param(limit.value());
	    return
		spec
		.query(invoiceMapper)
		.list();}}
}
