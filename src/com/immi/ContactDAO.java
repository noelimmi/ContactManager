package com.immi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement ;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ContactDAO {
	
	Connection connection;
	
	ContactDAO(){
		DbManager db = new DbManager();
		connection = db.getConnection();
	}
	
	public List<Contact> listAll() {
		try {
			List<Contact> data = new ArrayList<>();
	    	if(this.connection == null) {
	    		throw new SQLException("Connection not successfull");
	    	}
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from contacts");
			while(rs.next()) {
				Contact contact = new Contact();
				contact.setId(rs.getInt("id"));
				contact.setName(rs.getString("email"));
				contact.setemail(rs.getString("name"));
				data.add(contact);
			}
			st.close();
			connection.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	  
    public Contact get(int contactId) {
    	try {
	    	if(this.connection == null) {
	    		throw new SQLException("Connection not successfull");
	    	}
			Contact contact = new Contact();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from contacts where id="+contactId);
			if(!rs.next()) {
				connection.close();
				return null;
			}
			contact.setId(rs.getInt("id"));
			contact.setName(rs.getString("email"));
			contact.setemail(rs.getString("name"));	
			st.close();
			connection.close();
			return contact;
		} catch (SQLException e) {
			System.out.print("Connection to DB Failed");
			e.printStackTrace();
			return null;
		}
    }
     
    public Contact add(Contact contact) {
    	
    	try {
	    	if(this.connection == null) {
	    		throw new SQLException("Connection not successfull");
	    	}
			String sql = "INSERT INTO contacts (email,name) VALUES(?,?)";
			PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, contact.getemail());
			pstmt.setString(2, contact.getName());
			int rowAffected = pstmt.executeUpdate();
			if(rowAffected != 1)
			{
				return null;
			}
			int contactId = -1;
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next())
				contactId = rs.getInt(1);
			return this.get(contactId);
		} catch (SQLException e) {
			System.out.print("Connection to DB Failed");
			e.printStackTrace();
			return null;
		}
    }

    public boolean delete(int id) {
    	try {
    		if(this.connection == null) {
	    		throw new SQLException("Connection not successfull");
	    	}
    		final String SQL_DELETE = "DELETE FROM contacts WHERE id=?";
    		PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE,Statement.RETURN_GENERATED_KEYS);
    		preparedStatement.setInt(1,id);
    		int rowAffected = preparedStatement.executeUpdate();
    		connection.close();
    		if (rowAffected != 1) {
    			return false;
    		}
    		return true;
		} catch (SQLException e) {
			System.out.print("Connection to DB Failed");
			e.printStackTrace();
			return false;
		}
    }
     
    public boolean update(Contact contact) {
    	try {
    		if(this.connection == null) {
	    		throw new SQLException("Connection not successfull");
	    	}
    		final String SQL_UPDATE = "UPDATE contacts SET email=?,name=? WHERE id=?";
    		PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,Statement.RETURN_GENERATED_KEYS);
    		preparedStatement.setString(1,contact.getemail());
    		preparedStatement.setString(2,contact.getName());
    		preparedStatement.setInt(3,contact.getId());
    		int rowAffected = preparedStatement.executeUpdate();
    		connection.close();
    		if (rowAffected != 1) {
    			return false;
    		}
    		return true;
		} catch (SQLException e) {
			System.out.print("Connection to DB Failed");
			e.printStackTrace();
			return false;
		}
    }
}
