package com.immi;
 
//import java.net.*;
//import java.util.*;
 
import javax.ws.rs.*;
import javax.ws.rs.core.*;
 
 
@Path("/products")
public class ProductResource {
    //private ProductDAO dao = ProductDAO.getInstance();
     
    // RESTful API methods go here...  
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String helloWorld() {
	    return "{\"sample\":\"hello World\"}";
	}
}