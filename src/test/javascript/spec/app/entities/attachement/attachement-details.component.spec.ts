/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AttachementDetailComponent from '@/entities/attachement/attachement-details.vue';
import AttachementClass from '@/entities/attachement/attachement-details.component';
import AttachementService from '@/entities/attachement/attachement.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Attachement Management Detail Component', () => {
    let wrapper: Wrapper<AttachementClass>;
    let comp: AttachementClass;
    let attachementServiceStub: SinonStubbedInstance<AttachementService>;

    beforeEach(() => {
      attachementServiceStub = sinon.createStubInstance<AttachementService>(AttachementService);

      wrapper = shallowMount<AttachementClass>(AttachementDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { attachementService: () => attachementServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAttachement = { id: 123 };
        attachementServiceStub.find.resolves(foundAttachement);

        // WHEN
        comp.retrieveAttachement(123);
        await comp.$nextTick();

        // THEN
        expect(comp.attachement).toBe(foundAttachement);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachement = { id: 123 };
        attachementServiceStub.find.resolves(foundAttachement);

        // WHEN
        comp.beforeRouteEnter({ params: { attachementId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachement).toBe(foundAttachement);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
