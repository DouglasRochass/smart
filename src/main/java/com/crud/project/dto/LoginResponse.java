package com.crud.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long id;
    private String email;
    private String nomeColaborador;
    private String cargo;
    private String tipo;
    private Long mercadoId;
    private boolean ativo;
    private String token;
    private String message;

    // Manual getters/setters to avoid Lombok processing issues
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNomeColaborador() { return nomeColaborador; }
    public void setNomeColaborador(String nomeColaborador) { this.nomeColaborador = nomeColaborador; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getMercadoId() { return mercadoId; }
    public void setMercadoId(Long mercadoId) { this.mercadoId = mercadoId; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    // Manual builder to mirror Lombok's builder()
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final LoginResponse instance = new LoginResponse();
        public Builder id(Long id) { instance.setId(id); return this; }
        public Builder email(String email) { instance.setEmail(email); return this; }
        public Builder nomeColaborador(String nomeColaborador) { instance.setNomeColaborador(nomeColaborador); return this; }
        public Builder cargo(String cargo) { instance.setCargo(cargo); return this; }
        public Builder tipo(String tipo) { instance.setTipo(tipo); return this; }
        public Builder mercadoId(Long mercadoId) { instance.setMercadoId(mercadoId); return this; }
        public Builder ativo(boolean ativo) { instance.setAtivo(ativo); return this; }
        public Builder token(String token) { instance.setToken(token); return this; }
        public Builder message(String message) { instance.setMessage(message); return this; }
        public LoginResponse build() { return instance; }
    }
}
