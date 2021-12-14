import { element, by, ElementFinder } from 'protractor';

export class RessemblanceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-ressemblance div table .btn-danger'));
  title = element.all(by.css('perma-ressemblance div h2#page-heading span')).first();
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

export class RessemblanceUpdatePage {
  pageTitle = element(by.id('perma-ressemblance-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  descriptionInput = element(by.id('field_description'));

  planteRessemblantSelect = element(by.id('field_planteRessemblant'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async planteRessemblantSelectLastOption(): Promise<void> {
    await this.planteRessemblantSelect.all(by.tagName('option')).last().click();
  }

  async planteRessemblantSelectOption(option: string): Promise<void> {
    await this.planteRessemblantSelect.sendKeys(option);
  }

  getPlanteRessemblantSelect(): ElementFinder {
    return this.planteRessemblantSelect;
  }

  async getPlanteRessemblantSelectedOption(): Promise<string> {
    return await this.planteRessemblantSelect.element(by.css('option:checked')).getText();
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

export class RessemblanceDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-ressemblance-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-ressemblance'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
