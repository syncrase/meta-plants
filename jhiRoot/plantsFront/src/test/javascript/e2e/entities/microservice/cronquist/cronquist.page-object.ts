import { element, by, ElementFinder } from 'protractor';

export class CronquistComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-cronquist div table .btn-danger'));
  title = element.all(by.css('perma-cronquist div h2#page-heading span')).first();
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

export class CronquistUpdatePage {
  pageTitle = element(by.id('perma-cronquist-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  regneInput = element(by.id('field_regne'));
  sousRegneInput = element(by.id('field_sousRegne'));
  divisionInput = element(by.id('field_division'));
  classeInput = element(by.id('field_classe'));
  sousClasseInput = element(by.id('field_sousClasse'));
  ordreInput = element(by.id('field_ordre'));
  familleInput = element(by.id('field_famille'));
  genreInput = element(by.id('field_genre'));
  especeInput = element(by.id('field_espece'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

  async setDivisionInput(division: string): Promise<void> {
    await this.divisionInput.sendKeys(division);
  }

  async getDivisionInput(): Promise<string> {
    return await this.divisionInput.getAttribute('value');
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

  async setOrdreInput(ordre: string): Promise<void> {
    await this.ordreInput.sendKeys(ordre);
  }

  async getOrdreInput(): Promise<string> {
    return await this.ordreInput.getAttribute('value');
  }

  async setFamilleInput(famille: string): Promise<void> {
    await this.familleInput.sendKeys(famille);
  }

  async getFamilleInput(): Promise<string> {
    return await this.familleInput.getAttribute('value');
  }

  async setGenreInput(genre: string): Promise<void> {
    await this.genreInput.sendKeys(genre);
  }

  async getGenreInput(): Promise<string> {
    return await this.genreInput.getAttribute('value');
  }

  async setEspeceInput(espece: string): Promise<void> {
    await this.especeInput.sendKeys(espece);
  }

  async getEspeceInput(): Promise<string> {
    return await this.especeInput.getAttribute('value');
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

export class CronquistDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-cronquist-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-cronquist'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
