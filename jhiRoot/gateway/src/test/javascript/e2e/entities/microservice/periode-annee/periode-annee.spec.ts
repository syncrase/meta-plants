import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  PeriodeAnneeComponentsPage,
  /* PeriodeAnneeDeleteDialog, */
  PeriodeAnneeUpdatePage,
} from './periode-annee.page-object';

const expect = chai.expect;

describe('PeriodeAnnee e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let periodeAnneeComponentsPage: PeriodeAnneeComponentsPage;
  let periodeAnneeUpdatePage: PeriodeAnneeUpdatePage;
  /* let periodeAnneeDeleteDialog: PeriodeAnneeDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PeriodeAnnees', async () => {
    await navBarPage.goToEntity('periode-annee');
    periodeAnneeComponentsPage = new PeriodeAnneeComponentsPage();
    await browser.wait(ec.visibilityOf(periodeAnneeComponentsPage.title), 5000);
    expect(await periodeAnneeComponentsPage.getTitle()).to.eq('gatewayApp.microservicePeriodeAnnee.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(periodeAnneeComponentsPage.entities), ec.visibilityOf(periodeAnneeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create PeriodeAnnee page', async () => {
    await periodeAnneeComponentsPage.clickOnCreateButton();
    periodeAnneeUpdatePage = new PeriodeAnneeUpdatePage();
    expect(await periodeAnneeUpdatePage.getPageTitle()).to.eq('gatewayApp.microservicePeriodeAnnee.home.createOrEditLabel');
    await periodeAnneeUpdatePage.cancel();
  });

  /* it('should create and save PeriodeAnnees', async () => {
        const nbButtonsBeforeCreate = await periodeAnneeComponentsPage.countDeleteButtons();

        await periodeAnneeComponentsPage.clickOnCreateButton();

        await promise.all([
            periodeAnneeUpdatePage.debutSelectLastOption(),
            periodeAnneeUpdatePage.finSelectLastOption(),
        ]);

        await periodeAnneeUpdatePage.save();
        expect(await periodeAnneeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await periodeAnneeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last PeriodeAnnee', async () => {
        const nbButtonsBeforeDelete = await periodeAnneeComponentsPage.countDeleteButtons();
        await periodeAnneeComponentsPage.clickOnLastDeleteButton();

        periodeAnneeDeleteDialog = new PeriodeAnneeDeleteDialog();
        expect(await periodeAnneeDeleteDialog.getDialogTitle())
            .to.eq('gatewayApp.microservicePeriodeAnnee.delete.question');
        await periodeAnneeDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(periodeAnneeComponentsPage.title), 5000);

        expect(await periodeAnneeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
