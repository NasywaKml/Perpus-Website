package Perpuskaan.demo.entity;

public class Pustakawan {
    private int idPustakawan;
    private String namaPustakawan;
    private String passwordPustakawan;
    private String pustakawanEmail;

    public Pustakawan(){

    }
    public Pustakawan (int idPustakawan, String namaPustakawan, String passwordPustakawan, String pustakawanEmail){
        this.idPustakawan = idPustakawan;
        this.namaPustakawan = namaPustakawan;
        this.passwordPustakawan = passwordPustakawan;
        this.pustakawanEmail = pustakawanEmail;
    }

    
}
