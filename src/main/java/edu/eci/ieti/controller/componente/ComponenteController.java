package edu.eci.ieti.controller.componente;

import edu.eci.ieti.exception.UserNotFoundException;
import edu.eci.ieti.repository.*;
import edu.eci.ieti.repository.user.User;
import edu.eci.ieti.repository.user.UserDto;
import edu.eci.ieti.service.componente.ComponenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/componente/")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(@Autowired ComponenteService componenteService){
        this.componenteService = componenteService;
    }

    @PostMapping
    public ResponseEntity<Componente> createComponent(@RequestBody Componente componente) {
        URI createdUserUri = URI.create("");
        Componente cpu = new CPU(componente.getNombre());
        cpu.setPrecio(componente.getPrecio());
        String className = String.valueOf(componente.getClass());
        String name = componente.getNombre();
        Componente componente1 = null;
        switch (className) {
            case "Tarjeta_de_video" -> componente1 = new Tarjeta_de_video(name);
            case "CPU" -> componente1 = new CPU(name);
            case "RAM" -> componente1 = new RAM(name);
            case "Board" -> componente1 = new Board(name);
            case "Carcasa" -> componente1 = new Carcasa(name);
            case "Disco_Duro" -> componente1 = new Disco_Duro(name);
            case "Fuente_de_poder" -> componente1 = new Fuente_de_poder(name);
        }
        Board board = new Board("test");
        board.setSocket_CPU("1");board.setMemoria_maxima(2);board.setEspacio_de_memoria(2);board.setEspecificaciones("ss");
        board.setPrecio(12.5);
        System.out.println(componente1);
        if (componente1!=null) componenteService.save(board);
        return ResponseEntity.created(createdUserUri).body(componente1);
    }

    @GetMapping(value = "/getAllComponents")
    public ResponseEntity<List<Componente>> getAllComponent() {
        List<Componente> componentes= componenteService.all();
        return ResponseEntity.ok(componentes);
    }

    @GetMapping("{id}")
    public ResponseEntity<Componente> findById(@PathVariable("id") String id) {
        Optional<Componente> op = componenteService.findById(id);
        if (op.isEmpty()) throw new UserNotFoundException(id);
        return ResponseEntity.ok(op.get());
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<User> updateComponent(@PathVariable("id") String id, @RequestBody Componente componente) {
        Componente componente1 = new CPU(componente.getId());
        Optional<Componente> componentes = componenteService.findById(id);
        if (componentes.isPresent()){
            componenteService.update(componente1,id);
            componenteService.save(componentes.get());
            return ResponseEntity.ok(null);
        }else{
            throw new UserNotFoundException(id);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable("id") String id) {
        if(componenteService.findById(id).isEmpty()) throw new UserNotFoundException(id);
        componenteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
