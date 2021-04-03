package com.yfsanchez.springboot.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yfsanchez.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Nombre01 Apellido01","Nombre02 Apellido02","Nombre2.5 Apellido2.5","Nombre03 Apellido03","Nombre04 Apellido04","Bruce Lee","Bruce Willis")
				.map(nombre -> {
					return new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase());
				})
				.filter(usuario -> {
					return usuario.getNombre().toLowerCase().equals("bruce");
				})
				.doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("No se permite vacios.");
					}
					System.out.println(usuario.toString());
					})
				.map(usuario -> {
					String nombre = usuario.getNombre();
					usuario.setNombre(nombre.toLowerCase());
					return usuario;
				});
		
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("se ha ejecutado satifactoriamente.");
						
					}
				});
		
	}

}
