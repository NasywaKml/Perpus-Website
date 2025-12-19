![alt text](adjustStockBuku.png)
```
@startuml
title Buku - Adjust Stok Buku

actor Admin
entity Front_End
entity BukuController
entity BukuService
database Database

group Adjust Stok Buku
    Admin -> Front_End: update stok
    Front_End -> BukuController: PATCH /api/buku/{id}/stok\n(delta)
    activate BukuController

    BukuController -> BukuService: adjustStok(id, delta)
    activate BukuService

    BukuService -> Database: findById(id)
    activate Database
    Database --> BukuService: Buku
    deactivate Database

    BukuService -> BukuService: hitung stok baru
    BukuService -> BukuService: validasi stok >= 0
    BukuService -> BukuService: entity managed (@Transactional)

    BukuService -> BukuService: convertToDto()
    BukuService --> BukuController: BukuSearchResponseDto
    deactivate BukuService

    BukuController --> Front_End: HTTP 200 OK\n(Stok updated)
    deactivate BukuController

    Front_End --> Admin: update sukses
end
@enduml
```