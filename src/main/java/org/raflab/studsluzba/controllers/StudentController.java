package org.raflab.studsluzba.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.*;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.services.StudentService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path="/api/student")
public class StudentController {

	@Autowired
	StudentPodaciRepository studentPodaciRepository;
	
	@Autowired
	StudentIndeksRepository studentIndeksRepository;
	
	@Autowired
	StudentProfileService studentProfileService;

	@Autowired
	StudentIndeksService studentIndeksService;

	@Autowired
	StudijskiProgramRepository studijskiProgramRepository;

    @Autowired
    EntityMappers entityMappers;

    @Autowired
    private StudentService studentService;

	@PostMapping(path="/add")
   	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaci) {
    	
   	    StudentPodaci sp = studentPodaciRepository.save(Converters.toStudentPodaci(studentPodaci));
   	    
   	    return sp.getId();
 	}

	//Create
	@PostMapping
	public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO dto) {
		return ResponseEntity.ok(studentService.create(dto));
	}
	//Sci stidneti
	@GetMapping(path="/all")
	public List<StudentDTO> getAllStudents() {
		return studentService.getAllStudents();
	}
	//Studnet po ID
	@GetMapping("/{id}")
	public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {

		StudentDTO dto = studentService.getById(id);

		if (dto == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(dto);
	}
	//Brisi studenta po ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		studentService.delete(id);
		return ResponseEntity.noContent().build();
	}
	//Student podaci po indeksu studenta
	@GetMapping("/indeks")
	public ResponseEntity<StudentDTO> getByIndeks(
			@RequestParam int broj,
			@RequestParam int godina
	) {
		StudentDTO dto = studentService.getByIndex(broj, godina);

		if (dto == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(dto);
	}
	//Polozeni ispiti
	@GetMapping("/polozeni")
	public ResponseEntity<Page<IspitPolozenDTO>> getPolozeniIspiti(
			@RequestParam int broj,
			@RequestParam int godina,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Page<IspitPolozenDTO> dto = studentService.getPolozeniIspiti(broj, godina, page, size);

		return ResponseEntity.ok(dto);
	}
	//Nepolozeni ispiti
	@GetMapping("/nepolozeni")
	public ResponseEntity<Page<IspitNepolozeniDTO>> getNepolozeni(
			@RequestParam int broj,
			@RequestParam int godina,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Page<IspitNepolozeniDTO> dto = studentService.getNepolozeniIspiti(broj, godina, page, size);

		return ResponseEntity.ok(dto);
	}
	//Upisane godine
	@GetMapping("/upisane-godine")
	public ResponseEntity<List<UpisGodineDTO>> getUpisaneGodine(@RequestParam int broj, @RequestParam int godina) {
		List<UpisGodineDTO> result = studentService.getUpisaneGodine(broj, godina);

		return ResponseEntity.ok(result);
	}
	//Upisivanje studenata na novu godinu
	@PostMapping("/upisi-godinu")
	public ResponseEntity<UpisGodineDTO> upisiGodinu(@RequestBody UpisGodineRequest req) {
		UpisGodineDTO dto = studentService.upisiGodinu(req);
		return ResponseEntity.ok(dto);
	}
	//Pregled obnovljenih godina za indeks
	@GetMapping("/obnovljene-godine")
	public ResponseEntity<List<ObnovaGodineDTO>> getObnovljeneGodine(@RequestParam int broj, @RequestParam int godina) {
		List<ObnovaGodineDTO> result = studentService.getObnovljeneGodine(broj, godina);

		return ResponseEntity.ok(result);
	}
	//Obnova godine sa ESPB
	@PostMapping("/obnova-godine")
	public ResponseEntity<ObnovaGodineDTO> obnovaGodine(@RequestBody ObnovaGodineRequest req) {
		return ResponseEntity.ok(studentService.obnovaGodine(req));
	}
	//Uplate
	@PostMapping("/uplata")
	public ResponseEntity<UplataDTO> novaUplata(@RequestBody UplataRequest req) {
		return ResponseEntity.ok(studentService.dodajUplatu(req));
	}
	//Preostalo da se uplati
	@GetMapping("/preostalo-za-uplatu")
	public ResponseEntity<PreostalaUplataDTO> preostaloZaUplatu(@RequestParam int broj, @RequestParam int godina) {
		return ResponseEntity.ok(studentService.preostaloZaUplatu(broj, godina));
	}
	//Pretraga studenta po imenu illi prezimenu ili oba
	@GetMapping("/pretraga")
	public ResponseEntity<Page<StudentDTO>> pretragaStudenata(
			@RequestParam(required = false) String ime,
			@RequestParam(required = false) String prezime,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(studentService.pretraziStudente(ime, prezime, page, size));
	}
	//Pretraga poo srednjoj skoli
	@GetMapping("/pretraga-srednja-skola-naziv")
	public ResponseEntity<Page<StudentDTO>> pretragaPoNazivuSkole(
			@RequestParam String naziv,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(studentService.findByNazivSrednjeSkole(naziv, page, size));
	}






	@GetMapping(path="/svi")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentPodaciToResponse);
	}

	@GetMapping(path="/podaci/{id}")
	public StudentPodaciResponse getStudentPodaci(@PathVariable Long id){
		Optional<StudentPodaci> rez = studentPodaciRepository.findById(id);
		if(rez.isEmpty()) return null;
		return entityMappers.fromStudentPodaciToResponse(rez.get());
	}
    
    @PostMapping(path="/saveindeks")
   	public Long saveIndeks(@RequestBody StudentIndeksRequest request) {
		StudentIndeks studentIndeks = Converters.toStudentIndeks(request);

		int nextBroj = studentIndeksService.findBroj(request.getGodina(), request.getStudProgramOznaka());
		studentIndeks.setBroj(nextBroj);

		Optional<StudentPodaci> studentPodaci = studentPodaciRepository.findById(request.getStudentId());
		if(studentPodaci.isEmpty()) return null;		//TODO - throw error if not exist
		studentIndeks.setStudent(studentPodaci.get());

		List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka());
		if (studijskiProgrami.isEmpty()) {
            throw new RuntimeException("No StudijskiProgram found with oznaka: " + request.getStudProgramOznaka());
		}
		studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));

		try {
			StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
			return savedStudentIndeks.getId();
		} catch (DataIntegrityViolationException e) {
			// TODO
			throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
		} catch (Exception e) {
			throw new RuntimeException("Error while saving the StudentIndeks.", e);
		}
 	}
    
    @GetMapping(path="/indeks/{id}")
    public StudentIndeksResponse getStudentIndeks(@PathVariable Long id){
    	Optional<StudentIndeks> rez = studentIndeksRepository.findById(id);
    	if(rez.isEmpty()) return null;
    	else {
    		StudentIndeks retVal = rez.get();    		
    		return entityMappers.fromStudentIndexToResponse(retVal);
    	}
    	    	
    }
    
    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci)
                .stream()
                .map(entityMappers::fromStudentIndexToResponse) // map each entity to response
                .collect(Collectors.toList());
    }
    
    @GetMapping(path="/fastsearch")  // salje se string oblika rn1923 - smer godina broj
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
      String[] parsedData = ParseUtils.parseIndeks(indeksShort);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));	
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
    }
    
    @GetMapping(path="/emailsearch")  // salje se email studenta
    public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
      String[] parsedData = ParseUtils.parseEmail(studEmail);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));	
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
    }
    
    /*@GetMapping(path="/search")  // pretraga po imenu, prezimenu i elementima indeksa
    public Page<StudentDTO> search(@RequestParam (required = false) String ime,
								   @RequestParam (required = false) String prezime,
								   @RequestParam (required = false) String studProgram,
								   @RequestParam (required = false) Integer godina,
								   @RequestParam (required = false) Integer broj,
								   @RequestParam(defaultValue = "0") Integer page,
								   @RequestParam(defaultValue = "10") Integer size) {

		if(studProgram==null && godina == null && broj==null) { // pretrazivanje studenata bez indeksa
    		Page<StudentPodaci> spList = studentPodaciRepository.findStudent(ime, prezime, PageRequest.of(page, size, Sort.by("id").descending()));
			return spList.map(EntityMappers::fromStudentPodaciToDTO);
    	}
    	Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
		return siList.map(EntityMappers::fromStudentIndeksToDTO);
    }*/
    
    @GetMapping(path="/profile/{studentIndeksId}")  
    public StudentProfileDTO getStudentProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentProfile(studentIndeksId);   	
    }
    
    @GetMapping(path="/webprofile/{studentIndeksId}")  
    public StudentWebProfileDTO getStudentWebProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentWebProfile(studentIndeksId);   	
    }
    
    @GetMapping(path="/webprofile/email")  
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    	 String[] parsedData = ParseUtils.parseEmail(studEmail);
         if(parsedData!=null) {
       	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));
       	  if(si!=null)
       		  return studentProfileService.getStudentWebProfile(si.getId());   	
         }
         return null;
    }

}
