package com.salesianos.dam.sportify.noticia.service;

import com.salesianos.dam.sportify.comentario.repo.ComentarioRepository;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.repo.DeporteRepository;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.repo.EquipoRepository;
import com.salesianos.dam.sportify.files.model.FileMetadata;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.repo.LigaRepository;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.repo.NoticiaRepository;
import com.salesianos.dam.sportify.files.service.StorageService;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NoticiaServiceTest {

    @Mock
    private NoticiaRepository noticiaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private DeporteRepository deporteRepository;

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private LigaRepository ligaRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private NoticiaService noticiaService;

    private User user;
    private CreateNoticiaRequest createNoticiaRequest;

    @BeforeEach
    void setUp() {
        // Configurar los mocks
        user = new User();
        user.setUsername("testuser");

        // Crear un CreateNoticiaRequest con los valores necesarios
        createNoticiaRequest = new CreateNoticiaRequest(
                "Test Titular",
                "Test Cuerpo",
                "testuser",
                LocalDate.now(),
                "Deporte",
                "Equipo",
                "Liga"
        );
    }

    @Test
    void testSaveNoticia() {
        // Mock para los servicios
        when(userRepository.findFirstByUsername(anyString())).thenReturn(Optional.of(user));
        when(deporteRepository.findByNombreEqualsIgnoreCase(anyString())).thenReturn(Optional.of(new Deporte()));
        when(equipoRepository.findByNombreNoEspacio(anyString())).thenReturn(Optional.of(new Equipo()));
        when(ligaRepository.findByNombreNoEspacio(anyString())).thenReturn(Optional.of(new Liga()));

        // Mock de FileMetadata (evitar la instanciación directa)
        FileMetadata fileMetadataMock = mock(FileMetadata.class);
        when(fileMetadataMock.getFilename()).thenReturn("image.jpg");
        when(storageService.store(any(MultipartFile.class))).thenReturn(fileMetadataMock);

        Noticia noticia = noticiaService.saveNoticia(createNoticiaRequest, user, List.of(mock(MultipartFile.class)));

        // Verificaciones
        assertNotNull(noticia);
        assertEquals("Test Titular", noticia.getTitular());
        assertNotNull(noticia.getSlug());

        // Verificar que se llamaron los métodos esperados
        verify(noticiaRepository, times(1)).save(any(Noticia.class));
        verify(userRepository, times(1)).findFirstByUsername(anyString());
    }
}
