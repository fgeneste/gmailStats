/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import StatDetailComponent from '@/entities/stat/stat-details.vue';
import StatClass from '@/entities/stat/stat-details.component';
import StatService from '@/entities/stat/stat.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Stat Management Detail Component', () => {
    let wrapper: Wrapper<StatClass>;
    let comp: StatClass;
    let statServiceStub: SinonStubbedInstance<StatService>;

    beforeEach(() => {
      statServiceStub = sinon.createStubInstance<StatService>(StatService);

      wrapper = shallowMount<StatClass>(StatDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { statService: () => statServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundStat = { id: 123 };
        statServiceStub.find.resolves(foundStat);

        // WHEN
        comp.retrieveStat(123);
        await comp.$nextTick();

        // THEN
        expect(comp.stat).toBe(foundStat);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundStat = { id: 123 };
        statServiceStub.find.resolves(foundStat);

        // WHEN
        comp.beforeRouteEnter({ params: { statId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.stat).toBe(foundStat);
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
