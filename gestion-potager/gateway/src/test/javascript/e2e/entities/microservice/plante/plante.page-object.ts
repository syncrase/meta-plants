import { element, by, ElementFinder } from 'protractor';

export class PlanteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-plante div table .btn-danger'));
  title = element.all(by.css('gp-plante div h2#page-heading span')).first();
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
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PlanteUpdatePage {
  pageTitle = element(by.id('gp-plante-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nomLatinInput = element(by.id('field_nomLatin'));
  entretienInput = element(by.id('field_entretien'));
  histoireInput = element(by.id('field_histoire'));
  expositionInput = element(by.id('field_exposition'));
  rusticiteInput = element(by.id('field_rusticite'));

  cycleDeVieSelect = element(by.id('field_cycleDeVie'));
  classificationSelect = element(by.id('field_classification'));
  nomsVernaculairesSelect = element(by.id('field_nomsVernaculaires'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNomLatinInput(nomLatin: string): Promise<void> {
    await this.nomLatinInput.sendKeys(nomLatin);
  }

  async getNomLatinInput(): Promise<string> {
    return await this.nomLatinInput.getAttribute('value');
  }

  async setEntretienInput(entretien: string): Promise<void> {
    await this.entretienInput.sendKeys(entretien);
  }

  async getEntretienInput(): Promise<string> {
    return await this.entretienInput.getAttribute('value');
  }

  async setHistoireInput(histoire: string): Promise<void> {
    await this.histoireInput.sendKeys(histoire);
  }

  async getHistoireInput(): Promise<string> {
    return await this.histoireInput.getAttribute('value');
  }

  async setExpositionInput(exposition: string): Promise<void> {
    await this.expositionInput.sendKeys(exposition);
  }

  async getExpositionInput(): Promise<string> {
    return await this.expositionInput.getAttribute('value');
  }

  async setRusticiteInput(rusticite: string): Promise<void> {
    await this.rusticiteInput.sendKeys(rusticite);
  }

  async getRusticiteInput(): Promise<string> {
    return await this.rusticiteInput.getAttribute('value');
  }

  async cycleDeVieSelectLastOption(): Promise<void> {
    await this.cycleDeVieSelect.all(by.tagName('option')).last().click();
  }

  async cycleDeVieSelectOption(option: string): Promise<void> {
    await this.cycleDeVieSelect.sendKeys(option);
  }

  getCycleDeVieSelect(): ElementFinder {
    return this.cycleDeVieSelect;
  }

  async getCycleDeVieSelectedOption(): Promise<string> {
    return await this.cycleDeVieSelect.element(by.css('option:checked')).getText();
  }

  async classificationSelectLastOption(): Promise<void> {
    await this.classificationSelect.all(by.tagName('option')).last().click();
  }

  async classificationSelectOption(option: string): Promise<void> {
    await this.classificationSelect.sendKeys(option);
  }

  getClassificationSelect(): ElementFinder {
    return this.classificationSelect;
  }

  async getClassificationSelectedOption(): Promise<string> {
    return await this.classificationSelect.element(by.css('option:checked')).getText();
  }

  async nomsVernaculairesSelectLastOption(): Promise<void> {
    await this.nomsVernaculairesSelect.all(by.tagName('option')).last().click();
  }

  async nomsVernaculairesSelectOption(option: string): Promise<void> {
    await this.nomsVernaculairesSelect.sendKeys(option);
  }

  getNomsVernaculairesSelect(): ElementFinder {
    return this.nomsVernaculairesSelect;
  }

  async getNomsVernaculairesSelectedOption(): Promise<string> {
    return await this.nomsVernaculairesSelect.element(by.css('option:checked')).getText();
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

export class PlanteDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-plante-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-plante'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
