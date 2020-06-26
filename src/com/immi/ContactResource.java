package com.immi;
 

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;

import java.util.regex.Pattern; 

//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
import javax.ws.rs.core.*;
 
 
@Path("/contacts")
public class ContactResource {
    private ContactDAO dao = new ContactDAO();
    
    public boolean isValidEmail(Contact contact) {
    	String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$"; 
        Pattern pat = Pattern.compile(emailRegex); 
		if (contact.getemail() == null) return false; 
		return pat.matcher(contact.getemail()).matches() && contact.getemail().length() < 50 ; 
    }
    
    public boolean isValidName(Contact contact) {
		return contact.getName().length() < 50 ; 
    }
   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> getAll() {
        return dao.listAll();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
    	Contact contact = dao.get(id);
    	if (contact == null) {
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}
    	return Response.ok(contact, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Contact contact){
    	
    	List<Error> error= new ArrayList<>();
    	if(!this.isValidEmail(contact)) {
    		error.add(new Error("email","Provide a valid email and should be less than 50 characters."));
    	}
    	if(!this.isValidName(contact)) {
    		error.add(new Error("name","Provide a valid email and should be less than 45 characters."));
    	}
    	if(error.size()>0) {
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .type( MediaType.APPLICATION_JSON)
                    .build();
    	}
    	Contact newContact  = dao.add(contact);
    	if (contact == null) {
    		return Response.status(Response.Status.BAD_REQUEST).build();
    	}
    	return Response.ok(newContact, MediaType.APPLICATION_JSON).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id) {
        if (dao.delete(id)) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") int id, Contact contact) {
    	contact.setId(id);
    	List<Error> error= new ArrayList<>();
    	if(!this.isValidEmail(contact)) {
    		error.add(new Error("email","Provide a valid email and should be less than 50 characters."));
    	}
    	if(!this.isValidName(contact)) {
    		error.add(new Error("name","Provide a valid email and should be less than 45 characters."));
    	}
    	if(error.size()>0) {
    		return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .type( MediaType.APPLICATION_JSON)
                    .build();
    	}
        if (dao.update(contact)) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }
}