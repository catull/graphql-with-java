package com.graphqljava.tutorial.retail.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.ArgumentValue;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.JdbcClient.StatementSpec;
import org.springframework.stereotype.Controller;

import com.graphqljava.tutorial.retail.models.ChinookModels.Artist;
import com.graphqljava.tutorial.retail.models.RetailModels.account;
import com.graphqljava.tutorial.retail.models.RetailModels.order;
import com.graphqljava.tutorial.retail.models.RetailModels.order_detail;
import com.graphqljava.tutorial.retail.models.RetailModels.product;

public class ChinookControllers {
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
    
}
