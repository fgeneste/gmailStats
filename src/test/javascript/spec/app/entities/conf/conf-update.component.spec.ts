/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ConfUpdateComponent from '@/entities/conf/conf-update.vue';
import ConfClass from '@/entities/conf/conf-update.component';
import ConfService from '@/entities/conf/conf.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Conf Management Update Component', () => {
    let wrapper: Wrapper<ConfClass>;
    let comp: ConfClass;
    let confServiceStub: SinonStubbedInstance<ConfService>;

    beforeEach(() => {
      confServiceStub = sinon.createStubInstance<ConfService>(ConfService);

      wrapper = shallowMount<ConfClass>(ConfUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          confService: () => confServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.conf = entity;
        confServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(confServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.conf = entity;
        confServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(confServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundConf = { id: 123 };
        confServiceStub.find.resolves(foundConf);
        confServiceStub.retrieve.resolves([foundConf]);

        // WHEN
        comp.beforeRouteEnter({ params: { confId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.conf).toBe(foundConf);
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
