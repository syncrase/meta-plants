import { element, by, ElementFinder } from 'protractor';

export class SectionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-section div table .btn-danger'));
  title = element.all(by.css('perma-section div h2#page-heading span')).first();
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

export class SectionUpdatePage {
  pageTitle = element(by.id('perma-section-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousGenreSelect = element(by.id('field_sousGenre'));
  sectionSelect = element(by.id('field_section'));

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

  async sousGenreSelectLastOption(): Promise<void> {
    await this.sousGenreSelect.all(by.tagName('option')).last().click();
  }

  async sousGenreSelectOption(option: string): Promise<void> {
    await this.sousGenreSelect.sendKeys(option);
  }

  getSousGenreSelect(): ElementFinder {
    return this.sousGenreSelect;
  }

  async getSousGenreSelectedOption(): Promise<string> {
    return await this.sousGenreSelect.element(by.css('option:checked')).getText();
  }

  async sectionSelectLastOption(): Promise<void> {
    await this.sectionSelect.all(by.tagName('option')).last().click();
  }

  async sectionSelectOption(option: string): Promise<void> {
    await this.sectionSelect.sendKeys(option);
  }

  getSectionSelect(): ElementFinder {
    return this.sectionSelect;
  }

  async getSectionSelectedOption(): Promise<string> {
    return await this.sectionSelect.element(by.css('option:checked')).getText();
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

export class SectionDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-section-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-section'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
