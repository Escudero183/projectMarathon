/**
 * 
 */
package com.app.marathon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.marathon.model.UsuarioSistema;
import com.app.marathon.repository.UsuarioSistemaRepository;

/**
 * @author Linygn Escudero
 *
 */

@Service
public class UsuarioSistemaService {
	
	@Autowired
	private UsuarioSistemaRepository usuarioSistemaRepository;
	
	public UsuarioSistema insert(UsuarioSistema usuario) {
		return usuarioSistemaRepository.save(usuario);
	}
	
	public void update(UsuarioSistema item) {
		usuarioSistemaRepository.save(item);
	}
	
	public void delete (UsuarioSistema item) {
		usuarioSistemaRepository.save(item);		
	}
	
	public List<UsuarioSistema> findByUsuario(Integer idUsuario) {
		return (List<UsuarioSistema>) usuarioSistemaRepository.findByUsuario(idUsuario);
	}

}
