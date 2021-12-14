import { element, by, ElementFinder } from 'protractor';

export class GenreComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-genre div table .btn-danger'));
  title = element.all(by.css('perma-genre div h2#page-heading span')).first();
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

export class GenreUpdatePage {
  pageTitle = element(by.id('perma-genre-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousTribuSelect = element(by.id('field_sousTribu'));
  genreSelect = element(by.id('field_genre'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomFrInput(nomFr: string): Promise<void> {
    await this.nomFrInput.sendKeys(nomFr);
  }

  async getNomFrInput(): Promise<string> {
    return await this.nomFrInput.getAttribute('value');
  }

  async setNomLatinInput(nomLatin: string): Promise<void> {
    await this.nomLatinInput.sendKeys(nomLatin);
  }

  async getNomLatinInput(): Promise<string> {
    return await this.nomLatinInput.getAttribute('value');
  }

  async sousTribuSelectLastOption(): Promise<void> {
    await this.sousTribuSelect.all(by.tagName('option')).last().click();
  }

  async sousTribuSelectOption(option: string): Promise<void> {
    await this.sousTribuSelect.sendKeys(option);
  }

  getSousTribuSelect(): ElementFinder {
    return this.sousTribuSelect;
  }

  async getSousTribuSelectedOption(): Promise<string> {
    return await this.sousTribuSelect.element(by.css('option:checked')).getText();
  }

  async genreSelectLastOption(): Promise<void> {
    await this.genreSelect.all(by.tagName('option')).last().click();
  }

  async genreSelectOption(option: string): Promise<void> {
    await this.genreSelect.sendKeys(option);
  }

  getGenreSelect(): ElementFinder {
    return this.genreSelect;
  }

  async getGenreSelectedOption(): Promise<string> {
    return await this.genreSelect.element(by.css('option:checked')).getText();
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

export class GenreDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-genre-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-genre'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
