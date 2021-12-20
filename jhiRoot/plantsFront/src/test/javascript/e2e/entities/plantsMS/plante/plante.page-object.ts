import { element, by, ElementFinder } from 'protractor';

export class PlanteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-plante div table .btn-danger'));
  title = element.all(by.css('perma-plante div h2#page-heading span')).first();
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

export class PlanteUpdatePage {
  pageTitle = element(by.id('perma-plante-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  entretienInput = element(by.id('field_entretien'));
  histoireInput = element(by.id('field_histoire'));
  vitesseCroissanceInput = element(by.id('field_vitesseCroissance'));
  expositionInput = element(by.id('field_exposition'));

  cycleDeVieSelect = element(by.id('field_cycleDeVie'));
  solSelect = element(by.id('field_sol'));
  temperatureSelect = element(by.id('field_temperature'));
  racineSelect = element(by.id('field_racine'));
  strateSelect = element(by.id('field_strate'));
  feuillageSelect = element(by.id('field_feuillage'));
  nomsVernaculairesSelect = element(by.id('field_nomsVernaculaires'));
  planteSelect = element(by.id('field_plante'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

  async setVitesseCroissanceInput(vitesseCroissance: string): Promise<void> {
    await this.vitesseCroissanceInput.sendKeys(vitesseCroissance);
  }

  async getVitesseCroissanceInput(): Promise<string> {
    return await this.vitesseCroissanceInput.getAttribute('value');
  }

  async setExpositionInput(exposition: string): Promise<void> {
    await this.expositionInput.sendKeys(exposition);
  }

  async getExpositionInput(): Promise<string> {
    return await this.expositionInput.getAttribute('value');
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

  async solSelectLastOption(): Promise<void> {
    await this.solSelect.all(by.tagName('option')).last().click();
  }

  async solSelectOption(option: string): Promise<void> {
    await this.solSelect.sendKeys(option);
  }

  getSolSelect(): ElementFinder {
    return this.solSelect;
  }

  async getSolSelectedOption(): Promise<string> {
    return await this.solSelect.element(by.css('option:checked')).getText();
  }

  async temperatureSelectLastOption(): Promise<void> {
    await this.temperatureSelect.all(by.tagName('option')).last().click();
  }

  async temperatureSelectOption(option: string): Promise<void> {
    await this.temperatureSelect.sendKeys(option);
  }

  getTemperatureSelect(): ElementFinder {
    return this.temperatureSelect;
  }

  async getTemperatureSelectedOption(): Promise<string> {
    return await this.temperatureSelect.element(by.css('option:checked')).getText();
  }

  async racineSelectLastOption(): Promise<void> {
    await this.racineSelect.all(by.tagName('option')).last().click();
  }

  async racineSelectOption(option: string): Promise<void> {
    await this.racineSelect.sendKeys(option);
  }

  getRacineSelect(): ElementFinder {
    return this.racineSelect;
  }

  async getRacineSelectedOption(): Promise<string> {
    return await this.racineSelect.element(by.css('option:checked')).getText();
  }

  async strateSelectLastOption(): Promise<void> {
    await this.strateSelect.all(by.tagName('option')).last().click();
  }

  async strateSelectOption(option: string): Promise<void> {
    await this.strateSelect.sendKeys(option);
  }

  getStrateSelect(): ElementFinder {
    return this.strateSelect;
  }

  async getStrateSelectedOption(): Promise<string> {
    return await this.strateSelect.element(by.css('option:checked')).getText();
  }

  async feuillageSelectLastOption(): Promise<void> {
    await this.feuillageSelect.all(by.tagName('option')).last().click();
  }

  async feuillageSelectOption(option: string): Promise<void> {
    await this.feuillageSelect.sendKeys(option);
  }

  getFeuillageSelect(): ElementFinder {
    return this.feuillageSelect;
  }

  async getFeuillageSelectedOption(): Promise<string> {
    return await this.feuillageSelect.element(by.css('option:checked')).getText();
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

  async planteSelectLastOption(): Promise<void> {
    await this.planteSelect.all(by.tagName('option')).last().click();
  }

  async planteSelectOption(option: string): Promise<void> {
    await this.planteSelect.sendKeys(option);
  }

  getPlanteSelect(): ElementFinder {
    return this.planteSelect;
  }

  async getPlanteSelectedOption(): Promise<string> {
    return await this.planteSelect.element(by.css('option:checked')).getText();
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
  private dialogTitle = element(by.id('perma-delete-plante-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-plante'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
