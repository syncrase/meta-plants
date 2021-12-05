import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CycleDeVieComponentsPage, CycleDeVieDeleteDialog, CycleDeVieUpdatePage } from './cycle-de-vie.page-object';

const expect = chai.expect;

describe('CycleDeVie e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cycleDeVieComponentsPage: CycleDeVieComponentsPage;
  let cycleDeVieUpdatePage: CycleDeVieUpdatePage;
  let cycleDeVieDeleteDialog: CycleDeVieDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CycleDeVies', async () => {
    await navBarPage.goToEntity('cycle-de-vie');
    cycleDeVieComponentsPage = new CycleDeVieComponentsPage();
    await browser.wait(ec.visibilityOf(cycleDeVieComponentsPage.title), 5000);
    expect(await cycleDeVieComponentsPage.getTitle()).to.eq('gatewayApp.microserviceCycleDeVie.home.title');
    await browser.wait(ec.or(ec.visibilityOf(cycleDeVieComponentsPage.entities), ec.visibilityOf(cycleDeVieComponentsPage.noResult)), 1000);
  });

  it('should load create CycleDeVie page', async () => {
    await cycleDeVieComponentsPage.clickOnCreateButton();
    cycleDeVieUpdatePage = new CycleDeVieUpdatePage();
    expect(await cycleDeVieUpdatePage.getPageTitle()).to.eq('gatewayApp.microserviceCycleDeVie.home.createOrEditLabel');
    await cycleDeVieUpdatePage.cancel();
  });

  it('should create and save CycleDeVies', async () => {
    const nbButtonsBeforeCreate = await cycleDeVieComponentsPage.countDeleteButtons();

    await cycleDeVieComponentsPage.clickOnCreateButton();

    await promise.all([
      cycleDeVieUpdatePage.semisSelectLastOption(),
      cycleDeVieUpdatePage.apparitionFeuillesSelectLastOption(),
      cycleDeVieUpdatePage.floraisonSelectLastOption(),
      cycleDeVieUpdatePage.recolteSelectLastOption(),
      cycleDeVieUpdatePage.croissanceSelectLastOption(),
      cycleDeVieUpdatePage.maturiteSelectLastOption(),
      cycleDeVieUpdatePage.plantationSelectLastOption(),
      cycleDeVieUpdatePage.rempotageSelectLastOption(),
      cycleDeVieUpdatePage.reproductionSelectLastOption(),
    ]);

    await cycleDeVieUpdatePage.save();
    expect(await cycleDeVieUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cycleDeVieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CycleDeVie', async () => {
    const nbButtonsBeforeDelete = await cycleDeVieComponentsPage.countDeleteButtons();
    await cycleDeVieComponentsPage.clickOnLastDeleteButton();

    cycleDeVieDeleteDialog = new CycleDeVieDeleteDialog();
    expect(await cycleDeVieDeleteDialog.getDialogTitle()).to.eq('gatewayApp.microserviceCycleDeVie.delete.question');
    await cycleDeVieDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cycleDeVieComponentsPage.title), 5000);

    expect(await cycleDeVieComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
