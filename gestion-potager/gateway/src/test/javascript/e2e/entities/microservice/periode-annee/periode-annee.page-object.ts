import { element, by, ElementFinder } from 'protractor';

export class PeriodeAnneeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-periode-annee div table .btn-danger'));
  title = element.all(by.css('gp-periode-annee div h2#page-heading span')).first();
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
    return this.title.getAttribute('gpTranslate');
  }
}

export class PeriodeAnneeUpdatePage {
  pageTitle = element(by.id('gp-periode-annee-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));

  debutSelect = element(by.id('field_debut'));
  finSelect = element(by.id('field_fin'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('gpTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async debutSelectLastOption(): Promise<void> {
    await this.debutSelect.all(by.tagName('option')).last().click();
  }

  async debutSelectOption(option: string): Promise<void> {
    await this.debutSelect.sendKeys(option);
  }

  getDebutSelect(): ElementFinder {
    return this.debutSelect;
  }

  async getDebutSelectedOption(): Promise<string> {
    return await this.debutSelect.element(by.css('option:checked')).getText();
  }

  async finSelectLastOption(): Promise<void> {
    await this.finSelect.all(by.tagName('option')).last().click();
  }

  async finSelectOption(option: string): Promise<void> {
    await this.finSelect.sendKeys(option);
  }

  getFinSelect(): ElementFinder {
    return this.finSelect;
  }

  async getFinSelectedOption(): Promise<string> {
    return await this.finSelect.element(by.css('option:checked')).getText();
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

export class PeriodeAnneeDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-periodeAnnee-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-periodeAnnee'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('gpTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
