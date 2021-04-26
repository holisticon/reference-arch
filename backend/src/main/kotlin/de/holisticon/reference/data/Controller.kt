package de.holisticon.reference.rest.api

import de.holisticon.reference.data.ApplicationRepository
import de.holisticon.reference.data.Application
import de.holisticon.reference.data.ApplicationConverter
import de.holisticon.reference.rest.model.ApplicationDto
import io.swagger.annotations.ApiParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import javax.validation.Valid

@RestController
class ApplicationController (
    private val applicationRepository: ApplicationRepository,
    private val applicationConverter: ApplicationConverter
) : ApplicationsApi {

  companion object {
      val CONSTANT = "VALUE"
  }

  override fun createApplication(
      @ApiParam(value = "Stage of the application", required = true)
      @PathVariable("stage")
      stage: String,
      @ApiParam(value = "Application to create.", required = true)
      @Valid @RequestBody
      application: ApplicationDto
  ): ResponseEntity<String> {

    val entity: Application = applicationConverter.toEntity(application)
    val result: Application = applicationRepository.save(entity)
    val location: URI = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(result.id).toUri()

    return ResponseEntity.created(location).build()
  }

  override fun getApplications(
      @PathVariable("stage") stage: String,
      @RequestParam(value = "id", required = false)
      @Valid id: Optional<List<String>>,
      @RequestParam(value = "offset", required = false)
      @Valid offset: Optional<Int>,
      @RequestParam(value = "limit", required = false)
      @Valid limit: Optional<Int>): ResponseEntity<List<ApplicationDto>> {
    val all = applicationRepository.findAll()
    return ResponseEntity.ok(applicationConverter.toDtos(all))
  }

  fun getConstantValue(): String {
    return CONSTANT
  }
}
