import { element, by, ElementFinder } from 'protractor';

export class APGIIIPlanteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-apgiii-plante div table .btn-danger'));
  title = element.all(by.css('perma-apgiii-plante div h2#page-heading span')).first();
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

export class APGIIIPlanteUpdatePage {
  pageTitle = element(by.id('perma-apgiii-plante-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  ordreInput = element(by.id('field_ordre'));
  familleInput = element(by.id('field_famille'));
  sousFamilleInput = element(by.id('field_sousFamille'));
  tribuInput = element(by.id('field_tribu'));
  sousTribuInput = element(by.id('field_sousTribu'));
  genreInput = element(by.id('field_genre'));

  cladesSelect = element(by.id('field_clades'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

  async cladesSelectLastOption(): Promise<void> {
    await this.cladesSelect.all(by.tagName('option')).last().click();
  }

  async cladesSelectOption(option: string): Promise<void> {
    await this.cladesSelect.sendKeys(option);
  }

  getCladesSelect(): ElementFinder {
    return this.cladesSelect;
  }

  async getCladesSelectedOption(): Promise<string> {
    return await this.cladesSelect.element(by.css('option:checked')).getText();
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

export class APGIIIPlanteDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-aPGIIIPlante-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-aPGIIIPlante'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
