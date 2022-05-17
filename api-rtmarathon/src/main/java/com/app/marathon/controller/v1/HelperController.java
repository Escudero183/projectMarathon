/**
 * 
 */
package com.app.marathon.controller.v1;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.app.marathon.config.exception.ApiMarathonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Linygn Escudero
 *
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/helper", produces = { MediaType.APPLICATION_JSON_VALUE })
public class HelperController {
	
	public static final Locale LOCALE_MX = new Locale("es", "MX");
	public static final String pathApiCR = "https://dev.regionsanmartin.gob.pe/planificapp";
	public static final String pathApiRuc = "https://api.apis.net.pe/v1/ruc?numero=";
	public static final String tokenCR = "";
	public static final String pathApiRuc2 = "https://wsruc.com/Ruc2WS_JSON.php";
	public static final String tokenApiRuc2 = "cXdlcnR5bGFtYXJja19zYUBob3RtYWlsLmNvbXF3ZXJ0eQ==";
	
	@ApiOperation(value = "Obtiene datos de un ciudadano peruano a través de la Consulta Reniec", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping("/reniec/getDatosCiudadano/{dni}")
	public ResponseEntity<?> ConsultaReniec(@PathVariable(value = "dni") String dni, HttpServletRequest request) throws URISyntaxException {				
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			String contextCR = "/api/v1/herramientas/";
			RestTemplate restTemplate = new RestTemplate();
		    URI uri = new URI(pathApiCR + contextCR + dni);	    
		    
		    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		    
		    if(result.getStatusCodeValue() == 200) {
		    	return new ResponseEntity<>(result.getBody().toString(), HttpStatus.OK);
	    	}else {
	    		return new ResponseEntity<>("Ha ocurrido un error:" + pathApiCR + contextCR, HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiMarathonException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de una consulta Ruc", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping("/ruc/{ruc}")
	public ResponseEntity<?> ConsultaRuc(@PathVariable(value = "ruc") String ruc, HttpServletRequest request) throws URISyntaxException {				
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			
			RestTemplate restTemplate = new RestTemplate();
		    URI uri = new URI(pathApiRuc + ruc);	    
		    
		    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		    
		    if(result.getStatusCodeValue() == 200) {
		    	return new ResponseEntity<>(result.getBody().toString(), HttpStatus.OK);
	    	}else {
	    		return new ResponseEntity<>("Ha ocurrido un error:" + pathApiRuc + ruc, HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiMarathonException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Obtiene datos de una consulta Ruc de otra fuente", authorizations = {@Authorization(value = "apiKey") })
	@GetMapping("/ruc2/{ruc}")
	public ResponseEntity<?> ConsultaRuc2(
			@PathVariable(value = "ruc") String ruc, 
			@RequestParam(value = "tipo", required = false, defaultValue = "2") Integer tipo,
			@RequestParam(value = "token", required = false, defaultValue = "") String token,
			HttpServletRequest request) throws URISyntaxException {				
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			if(token.equals("")) {
				token = tokenApiRuc2;
			}
			
			RestTemplate restTemplate = new RestTemplate();
		    URI uri = new URI(pathApiRuc2 + "?tipo=" + tipo + "&token=" + token + "&ruc=" + ruc);	    
		    
		    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
		    
		    if(result.getStatusCodeValue() == 200) {
		    	return new ResponseEntity<>(result.getBody().toString(), HttpStatus.OK);
	    	}else {
	    		return new ResponseEntity<>("Ha ocurrido un error:" + pathApiRuc + ruc, HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
			
		} catch (Exception ex) {
			return new ResponseEntity<>(new ApiMarathonException(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
