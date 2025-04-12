package views;

import data.Persistencia;
import domain.*;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import javax.swing.JOptionPane;

public class Controlador {
    
    
    
    public Controlador(){
    
        
        
       
    }
    
    public static TipoAlimentacion[] getTiposAlimentacion(){
        return  TipoAlimentacion.values();
    }
    public static ArrayList<Especie> getEspecies(){
        return Persistencia.getEspecies();
    }
    public static ArrayList<Sector> getSectores(){
        return Persistencia.getSectores();
    }
    public static ArrayList<Pais> getPaises(){
        return Persistencia.getPaises();
    }    
    public static ArrayList<AnimalViewModel> getAnimales(){
        ArrayList<AnimalViewModel> animales = new ArrayList<>();
        for(Mamifero animal : Persistencia.getAnimales()){
            animales.add(new AnimalViewModel(animal));
        }
        return animales;
    }
    
    public static ComidaViewModel  calcularComida(){
           double totalCarnivoros = Persistencia.getTotalComida(TipoAlimentacion.CARNIVORO);
           double totalHerbivoros = Persistencia.getTotalComida(TipoAlimentacion.HERBIVORO);
        return new ComidaViewModel(totalCarnivoros, totalHerbivoros);
    }
    
   
    
public static void botonGuardar(AnimalNuevo vista) {

    try {

        Especie especieSeleccionada = vista.getEspecieSeleccionada();

        Sector sectorSeleccionado = vista.getSectorSeleccionada();

        Pais paisSeleccionado = vista.getPaisSeleccionada();

        int edad = Integer.parseInt(vista.getFieldEdad().getText());

        double peso = Double.parseDouble(vista.getFieldPeso().getText());

        if (especieSeleccionada == null || sectorSeleccionado == null || paisSeleccionado == null) {
            throw new Exception("Por favor seleccioná una especie, un sector y un país.");
        }

        String nombre = especieSeleccionada.getNombre().toLowerCase();

        
        int sector = sectorSeleccionado.getNumero();

        

        if ((nombre.equals("león") || nombre.equals("tigre")) && (sector != 2 && sector != 4)) {

            throw new Exception("Especie no coincide con sector (carnívoros deben ir a sector 2 o 4).");

        }

        if ((nombre.equals("elefante") || nombre.equals("jirafa")) && (sector != 1 && sector != 3)) {

            throw new Exception("Especie no coincide con sector (herbívoros deben ir a sector 1 o 3).");

        }


        guardarAnimal(nombre ,paisSeleccionado, edad, peso, sectorSeleccionado);

        

        JOptionPane.showMessageDialog(null, "Animal guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {

        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

    }

} 

public static void guardarAnimal(String especieNombre, Pais paisNombre, int edad, double peso, Sector sector) {
   try {
       Especie especie = Persistencia.getEspeciePorNombre(especieNombre);
       
       TipoAlimentacion tipo = especie.getTipoAlimentacion();
       if (tipo == TipoAlimentacion.CARNIVORO) {
           Mamifero animal = new Carnivoro(edad, peso, especie, sector, paisNombre);
           Persistencia.agregarAnimal(animal);
       } else if (tipo == TipoAlimentacion.HERBIVORO) {
          
           double valorFijo = 100; 
           Mamifero animal = new Herbivoro(edad, peso, especie, sector, valorFijo, paisNombre);
           Persistencia.agregarAnimal(animal);
       }
   } catch (InvalidPropertiesFormatException | IllegalArgumentException ex) {
      
       JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
   }
}
    
}