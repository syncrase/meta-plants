import { element, by, ElementFinder } from 'protractor';

export class CronquistPlanteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-cronquist-plante div table .btn-danger'));
  title = element.all(by.css('perma-cronquist-plante div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class CronquistPlanteUpdatePage {
  pageTitle = element(by.id('perma-cronquist-plante-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  superRegneInput = element(by.id('field_superRegne'));
  regneInput = element(by.id('field_regne'));
  sousRegneInput = element(by.id('field_sousRegne'));
  rameauInput = element(by.id('field_rameau'));
  infraRegneInput = element(by.id('field_infraRegne'));
  superDivisionInput = element(by.id('field_superDivision'));
  divisionInput = element(by.id('field_division'));
  sousDivisionInput = element(by.id('field_sousDivision'));
  infraEmbranchementInput = element(by.id('field_infraEmbranchement'));
  microEmbranchementInput = element(by.id('field_microEmbranchement'));
  superClasseInput = element(by.id('field_superClasse'));
  classeInput = element(by.id('field_classe'));
  sousClasseInput = element(by.id('field_sousClasse'));
  infraClasseInput = element(by.id('field_infraClasse'));
  superOrdreInput = element(by.id('field_superOrdre'));
  ordreInput = element(by.id('field_ordre'));
  sousOrdreInput = element(by.id('field_sousOrdre'));
  infraOrdreInput = element(by.id('field_infraOrdre'));
  microOrdreInput = element(by.id('field_microOrdre'));
  superFamilleInput = element(by.id('field_superFamille'));
  familleInput = element(by.id('field_famille'));
  sousFamilleInput = element(by.id('field_sousFamille'));
  tribuInput = element(by.id('field_tribu'));
  sousTribuInput = element(by.id('field_sousTribu'));
  genreInput = element(by.id('field_genre'));
  sousGenreInput = element(by.id('field_sousGenre'));
  sectionInput = element(by.id('field_section'));
  sousSectionInput = element(by.id('field_sousSection'));
  especeInput = element(by.id('field_espece'));
  sousEspeceInput = element(by.id('field_sousEspece'));
  varieteInput = element(by.id('field_variete'));
  sousVarieteInput = element(by.id('field_sousVariete'));
  formeInput = element(by.id('field_forme'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSuperRegneInput(superRegne: string): Promise<void> {
    await this.superRegneInput.sendKeys(superRegne);
  }

  async getSuperRegneInput(): Promise<string> {
    return await this.superRegneInput.getAttribute('value');
  }

  async setRegneInput(regne: string): Promise<void> {
    await this.regneInput.sendKeys(regne);
  }

  async getRegneInput(): Promise<string> {
    return await this.regneInput.getAttribute('value');
  }

  async setSousRegneInput(sousRegne: string): Promise<void> {
    await this.sousRegneInput.sendKeys(sousRegne);
  }

  async getSousRegneInput(): Promise<string> {
    return await this.sousRegneInput.getAttribute('value');
  }

  async setRameauInput(rameau: string): Promise<void> {
    await this.rameauInput.sendKeys(rameau);
  }

  async getRameauInput(): Promise<string> {
    return await this.rameauInput.getAttribute('value');
  }

  async setInfraRegneInput(infraRegne: string): Promise<void> {
    await this.infraRegneInput.sendKeys(infraRegne);
  }

  async getInfraRegneInput(): Promise<string> {
    return await this.infraRegneInput.getAttribute('value');
  }

  async setSuperDivisionInput(superDivision: string): Promise<void> {
    await this.superDivisionInput.sendKeys(superDivision);
  }

  async getSuperDivisionInput(): Promise<string> {
    return await this.superDivisionInput.getAttribute('value');
  }

  async setDivisionInput(division: string): Promise<void> {
    await this.divisionInput.sendKeys(division);
  }

  async getDivisionInput(): Promise<string> {
    return await this.divisionInput.getAttribute('value');
  }

  async setSousDivisionInput(sousDivision: string): Promise<void> {
    await this.sousDivisionInput.sendKeys(sousDivision);
  }

  async getSousDivisionInput(): Promise<string> {
    return await this.sousDivisionInput.getAttribute('value');
  }

  async setInfraEmbranchementInput(infraEmbranchement: string): Promise<void> {
    await this.infraEmbranchementInput.sendKeys(infraEmbranchement);
  }

  async getInfraEmbranchementInput(): Promise<string> {
    return await this.infraEmbranchementInput.getAttribute('value');
  }

  async setMicroEmbranchementInput(microEmbranchement: string): Promise<void> {
    await this.microEmbranchementInput.sendKeys(microEmbranchement);
  }

  async getMicroEmbranchementInput(): Promise<string> {
    return await this.microEmbranchementInput.getAttribute('value');
  }

  async setSuperClasseInput(superClasse: string): Promise<void> {
    await this.superClasseInput.sendKeys(superClasse);
  }

  async getSuperClasseInput(): Promise<string> {
    return await this.superClasseInput.getAttribute('value');
  }

  async setClasseInput(classe: string): Promise<void> {
    await this.classeInput.sendKeys(classe);
  }

  async getClasseInput(): Promise<string> {
    return await this.classeInput.getAttribute('value');
  }

  async setSousClasseInput(sousClasse: string): Promise<void> {
    await this.sousClasseInput.sendKeys(sousClasse);
  }

  async getSousClasseInput(): Promise<string> {
    return await this.sousClasseInput.getAttribute('value');
  }

  async setInfraClasseInput(infraClasse: string): Promise<void> {
    await this.infraClasseInput.sendKeys(infraClasse);
  }

  async getInfraClasseInput(): Promise<string> {
    return await this.infraClasseInput.getAttribute('value');
  }

  async setSuperOrdreInput(superOrdre: string): Promise<void> {
    await this.superOrdreInput.sendKeys(superOrdre);
  }

  async getSuperOrdreInput(): Promise<string> {
    return await this.superOrdreInput.getAttribute('value');
  }

  async setOrdreInput(ordre: string): Promise<void> {
    await this.ordreInput.sendKeys(ordre);
  }

  async getOrdreInput(): Promise<string> {
    return await this.ordreInput.getAttribute('value');
  }

  async setSousOrdreInput(sousOrdre: string): Promise<void> {
    await this.sousOrdreInput.sendKeys(sousOrdre);
  }

  async getSousOrdreInput(): Promise<string> {
    return await this.sousOrdreInput.getAttribute('value');
  }

  async setInfraOrdreInput(infraOrdre: string): Promise<void> {
    await this.infraOrdreInput.sendKeys(infraOrdre);
  }

  async getInfraOrdreInput(): Promise<string> {
    return await this.infraOrdreInput.getAttribute('value');
  }

  async setMicroOrdreInput(microOrdre: string): Promise<void> {
    await this.microOrdreInput.sendKeys(microOrdre);
  }

  async getMicroOrdreInput(): Promise<string> {
    return await this.microOrdreInput.getAttribute('value');
  }

  async setSuperFamilleInput(superFamille: string): Promise<void> {
    await this.superFamilleInput.sendKeys(superFamille);
  }

  async getSuperFamilleInput(): Promise<string> {
    return await this.superFamilleInput.getAttribute('value');
  }

  async setFamilleInput(famille: string): Promise<void> {
    await this.familleInput.sendKeys(famille);
  }

  async getFamilleInput(): Promise<string> {
    return await this.familleInput.getAttribute('value');
  }

  async setSousFamilleInput(sousFamille: string): Promise<void> {
    await this.sousFamilleInput.sendKeys(sousFamille);
  }

  async getSousFamilleInput(): Promise<string> {
    return await this.sousFamilleInput.getAttribute('value');
  }

  async setTribuInput(tribu: string): Promise<void> {
    await this.tribuInput.sendKeys(tribu);
  }

  async getTribuInput(): Promise<string> {
    return await this.tribuInput.getAttribute('value');
  }

  async setSousTribuInput(sousTribu: string): Promise<void> {
    await this.sousTribuInput.sendKeys(sousTribu);
  }

  async getSousTribuInput(): Promise<string> {
    return await this.sousTribuInput.getAttribute('value');
  }

  async setGenreInput(genre: string): Promise<void> {
    await this.genreInput.sendKeys(genre);
  }

  async getGenreInput(): Promise<string> {
    return await this.genreInput.getAttribute('value');
  }

  async setSousGenreInput(sousGenre: string): Promise<void> {
    await this.sousGenreInput.sendKeys(sousGenre);
  }

  async getSousGenreInput(): Promise<string> {
    return await this.sousGenreInput.getAttribute('value');
  }

  async setSectionInput(section: string): Promise<void> {
    await this.sectionInput.sendKeys(section);
  }

  async getSectionInput(): Promise<string> {
    return await this.sectionInput.getAttribute('value');
  }

  async setSousSectionInput(sousSection: string): Promise<void> {
    await this.sousSectionInput.sendKeys(sousSection);
  }

  async getSousSectionInput(): Promise<string> {
    return await this.sousSectionInput.getAttribute('value');
  }

  async setEspeceInput(espece: string): Promise<void> {
    await this.especeInput.sendKeys(espece);
  }

  async getEspeceInput(): Promise<string> {
    return await this.especeInput.getAttribute('value');
  }

  async setSousEspeceInput(sousEspece: string): Promise<void> {
    await this.sousEspeceInput.sendKeys(sousEspece);
  }

  async getSousEspeceInput(): Promise<string> {
    return await this.sousEspeceInput.getAttribute('value');
  }

  async setVarieteInput(variete: string): Promise<void> {
    await this.varieteInput.sendKeys(variete);
  }

  async getVarieteInput(): Promise<string> {
    return await this.varieteInput.getAttribute('value');
  }

  async setSousVarieteInput(sousVariete: string): Promise<void> {
    await this.sousVarieteInput.sendKeys(sousVariete);
  }

  async getSousVarieteInput(): Promise<string> {
    return await this.sousVarieteInput.getAttribute('value');
  }

  async setFormeInput(forme: string): Promise<void> {
    await this.formeInput.sendKeys(forme);
  }

  async getFormeInput(): Promise<string> {
    return await this.formeInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CronquistPlanteDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-cronquistPlante-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-cronquistPlante'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
