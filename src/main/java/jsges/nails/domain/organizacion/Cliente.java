package jsges.nails.domain.organizacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cliente implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(columnDefinition = "TEXT")

        String razonSocial;
        int estado;

        @Column(columnDefinition = "TEXT")
        String letra;

        @Column(columnDefinition = "TEXT")
        String contacto;

        @Column(columnDefinition = "TEXT")
        String celular;
        @Column(columnDefinition = "TEXT")
        String mail;

        Date fechaInicio;
        Date fechaNacimiento;
        
        
        public void setLetra(String nombre){
            this.letra = nombre;
            
        }
        public void setRazonSocial(String razon){
            this.razonSocial = razon;
            
        }
        public void setContacto(String contacto){
            this.contacto = contacto;
            
        }
        public void setFechaInicio(Date fechaI){
            this.fechaInicio = fechaI;
            
        }
        public void setFechaNacimineto(Date fechaN){
            this.fechaNacimiento = fechaN;
        }
        public void setCelular(String celular){
            this.celular = celular;
        }
        public void setMail(String mail){
            this.mail = mail;
            
        }
        
        public String getMail(){
            return this.mail;
        }
        public String getCelular(){
            return this.celular;
        }
}
